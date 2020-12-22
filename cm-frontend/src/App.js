import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Route, BrowserRouter as Router, useHistory } from "react-router-dom";
import ProductList from "./components/ProductList";
import Banner from "./components/Banner";
import Upload from "./components/Upload";
import ProductDetail from "./components/ProductDetail";
import TopNav from "./components/TopNav";
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import DataService from "./services/DataService";
import useStickyState from './utils/useStickyState';



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

function ProductListContainer(props) {

  const [showBanner, setShowBanner] = useStickyState(true, 'dcm-banner');
  const [productList, setProductList] = React.useState([]);
  const [productListLoading, setProductListLoading] = React.useState(false);

  const setBannerClosed = () => {
    setShowBanner(false);
  }
  const setBannerOpened = () => {
    setShowBanner(true);
  }

  const history = useHistory();
  const searchAction = (s) => {
    history.push('/');
    loadProducts(s);
  }

  React.useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts(search) {
    setProductListLoading(true);
    await DataService.fetchProducts(search).then(response => {
        let body = response.data;
        setProductList(body);
        setProductListLoading(false);
      }
    ).catch( error => {
      console.log('Error occured: '+error.message);
      setProductListLoading(false);
    });
  }

  return (
    <>
    <TopNav aboutAction={setBannerOpened} searchAction={searchAction} />
    <Banner opened={showBanner} closeAction={setBannerClosed} />
    <Route exact path="/" >
      <ProductList list={productList} isLoading={productListLoading} refreshAction = {() => searchAction('')} />
    </Route>
    <Route exact path="/upload" component={Upload} />
    <Route path="/product/view/:id">
      <ProductDetail navigator = { listNavigator(productList, 1) }/>
    </Route>
    </>
  )
}

function App() {

  const classes = useStyles();
  return (
    <React.StrictMode>
      <MuiThemeProvider theme={theme}>
        <div className={classes.layoutWrapper}>
          <div className={classes.flexWrapper}>
            <Router>
              <ProductListContainer/>
            </Router>
          </div>
        </div>
      </MuiThemeProvider>
    </React.StrictMode>
  );
}

export default App;
