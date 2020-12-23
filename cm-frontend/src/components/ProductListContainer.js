import React from "react";
import { Route, useHistory } from "react-router-dom";
import ProductListPage from "../pages/ProductListPage";
import Banner from "./Banner";
import UploadPage from "../pages/UploadPage";
import ProductDetailPage from "../pages/ProductDetailPage";
import TopNav from "./TopNav";
import DataService from "../services/DataService";
import useStickyState from '../utils/useStickyState';

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
  
  export const listNavigator = (list) => {
    return {
      hasNext: (id) => { return currentPosition(list, id) < list.length - 1},
      hasPrevious: (id) => { return currentPosition(list, id) > 0},
      getNext: (id) => { return '/product/view/'+ list[currentPosition(list, id)+1].id},
      getPrevious: (id) => { return '/product/view/'+ list[currentPosition(list, id)-1].id},
    }
  }

export function ProductListContainer(props) {

  const [showBanner, setShowBanner] = useStickyState(true, 'dcm-banner');
  const [productList, setProductList] = React.useState([]);
  const [productListLoading, setProductListLoading] = React.useState(false);

  const setBannerClosed = () => {
    setShowBanner(false);
  };
  const setBannerOpened = () => {
    setShowBanner(true);
  };

  const history = useHistory();
  const searchAction = (s) => {
    history.push('/');
    loadProducts(s);
  };

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
    ).catch(error => {
      console.log('Error occured: ' + error.message);
      setProductListLoading(false);
    });
  }

  return (
    <>
      <TopNav aboutAction={setBannerOpened} searchAction={searchAction} />
      <Banner opened={showBanner} closeAction={setBannerClosed} />
      <Route exact path="/">
        <ProductListPage list={productList} isLoading={productListLoading} refreshAction={() => searchAction('')} />
      </Route>
      <Route exact path="/upload" component={UploadPage} />
      <Route path="/product/view/:id">
        <ProductDetailPage navigator={listNavigator(productList, 1)} />
      </Route>
    </>
  );
}
