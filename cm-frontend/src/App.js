import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Route, BrowserRouter as Router } from "react-router-dom";
import Table from "./components/Table";
import Upload from "./components/Upload";
import ProductDetail from "./components/ProductDetail";
import TopNav from "./components/TopNav";
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

const theme = createMuiTheme({
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
  return (
    <React.StrictMode>
      <MuiThemeProvider theme={theme}>
        <div className={classes.layoutWrapper}>
          <div className={classes.flexWrapper}>
          <Router>
            <TopNav></TopNav>
            <Route exact path="/" component={Table} />
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
