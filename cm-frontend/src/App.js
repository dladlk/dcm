import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Route, BrowserRouter as Router } from "react-router-dom";
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import GenerateCatalogPage from "./pages/GenerateCatalogPage";
import { ProductListContainer } from "./components/ProductListContainer";
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
    [theme.breakpoints.up('lg')]: {
    maxWidth: '70%',
    flex: '1 1 70%',
    },
    [theme.breakpoints.down('md')]: {
      maxWidth: '95%',
      flex: '1 1 100px',
    },
  },
}));

function App() {

  const classes = useStyles();
  return (
    <React.StrictMode>
      <MuiThemeProvider theme={theme}>
        <div className={classes.layoutWrapper}>
          <div className={classes.flexWrapper}>
            <Router basename={"/dcm"}>
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
