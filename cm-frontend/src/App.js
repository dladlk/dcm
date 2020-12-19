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
  const onSearchSubmit = (e) => {
    loadProducts();
  }

  React.useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    setProductListLoading(true);
    let response = await DataService.fetchProducts();
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
              <TopNav aboutAction={setBannerOpened} searchAction={onSearchSubmit} />
              <Banner opened={showBanner} closeAction={setBannerClosed} />
              <Route exact path="/" >
                <ProductList list={productList} isLoading={productListLoading} />
              </Route>
              <Route exact path="/upload" component={Upload} />
              <Route path="/product/view/:id" component={ProductDetail} />
            </Router>
          </div>
        </div>
      </MuiThemeProvider>
    </React.StrictMode>
  );
}

export default App;
