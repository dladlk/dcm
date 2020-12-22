import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Route, BrowserRouter as Router, useHistory } from "react-router-dom";
import ProductListPage from "./pages/ProductListPage";
import Banner from "./components/Banner";
import UploadPage from "./pages/UploadPage";
import ProductDetailPage from "./pages/ProductDetailPage";
import TopNav from "./components/TopNav";
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import DataService from "./services/DataService";
import useStickyState from './utils/useStickyState';



import './App.css';
import GenerateCatalogPage from "./pages/GenerateCatalogPage";

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

const currentPosition = (list, id) => {
  if (list._cachedPos) {
    if (list._cachedPos[id]) {
      return list._cachedPos[id] - 1; // Cache position with + 1 - so 0 is not considered as absent
    }
  } else {
    list._cachedPos = {};
  }
  if (!list) {
    return 0;
  }
  for (var i = 0; i < list.length; i++) if (list[i].id === id) {
    list._cachedPos[id] = (i + 1);
    // console.log('Current pos: '+i); 
    return i;
  };
  return 0;
};

const listNavigator = (list) => {
  return {
    hasNext: (id) => { return currentPosition(list, id) < list.length - 1},
    hasPrevious: (id) => { return currentPosition(list, id) > 0},
    getNext: (id) => { return '/product/view/'+ list[currentPosition(list, id)+1].id},
    getPrevious: (id) => { return '/product/view/'+ list[currentPosition(list, id)-1].id},
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
      <ProductListPage list={productList} isLoading={productListLoading} refreshAction = {() => searchAction('')} />
    </Route>
    <Route exact path="/upload" component={UploadPage} />
    <Route path="/product/view/:id">
      <ProductDetailPage navigator = { listNavigator(productList, 1) }/>
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
              <Route exact path="/generate">
                <GenerateCatalogPage />
              </Route>
            </Router>
          </div>
        </div>
      </MuiThemeProvider>
    </React.StrictMode>
  );
}

export default App;
