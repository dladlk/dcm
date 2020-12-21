import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Route, BrowserRouter as Router } from "react-router-dom";
import ProductList from "./components/ProductList";
import Banner from "./components/Banner";
import Upload from "./components/Upload";
import ProductDetail from "./components/ProductDetail";
import TopNav from "./components/TopNav";
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import DataService from "./services/DataService";


import './App.css';

const theme = createMuiTheme({
  typography: {
    fontFamily: ['IBM Plex Sans']
  },
  palette: {
    primary: {
      main: '#0B78D0'
    },
  }
});

const listNavigator = (list, currentPosition) => {
  return {
    hasNext: () => { return currentPosition < list.length - 1},
    hasPrevious: () => { return currentPosition > 0},
    getNext: () => { return '/product/view/'+ list[currentPosition++].id},
    getPrevious: () => { return '/product/view/'+ list[currentPosition--].id},
  }
}

const useStyles = makeStyles(theme => ({
  layoutWrapper: {
    display: 'flex',
    flexFlow: 'row wrap',
    alignItems: "center",
    placeContent: 'stretch center',
  },
  flexWrapper: {
    maxWidth: '70%',
    flex: '1 1 70%',
  },
}));

function App() {

  const classes = useStyles();

  const [showBanner, setShowBanner] = React.useState(true);
  const [productList, setProductList] = React.useState([]);
  const [productListLoading, setProductListLoading] = React.useState(false);

  const setBannerClosed = () => {
    setShowBanner(false);
  }
  const setBannerOpened = () => {
    setShowBanner(true);
  }
  const searchAction = (s) => {
    loadProducts(s);
  }

  React.useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts(search) {
    setProductListLoading(true);
    let response = await DataService.fetchProducts(search);
    let body = await response.json();
    setProductList(body);
    setProductListLoading(false);
  }

  return (
    <React.StrictMode>
      <MuiThemeProvider theme={theme}>
        <div className={classes.layoutWrapper}>
          <div className={classes.flexWrapper}>
            <Router>
              <TopNav aboutAction={setBannerOpened} searchAction={searchAction} />
              <Banner opened={showBanner} closeAction={setBannerClosed} />
              <Route exact path="/" >
                <ProductList list={productList} isLoading={productListLoading} refreshAction = {() => searchAction('')} />
              </Route>
              <Route exact path="/upload" component={Upload} />
              <Route path="/product/view/:id">
                <ProductDetail navigator = { listNavigator(productList, 1) }/>
              </Route>
            </Router>
          </div>
        </div>
      </MuiThemeProvider>
    </React.StrictMode>
  );
}

export default App;
