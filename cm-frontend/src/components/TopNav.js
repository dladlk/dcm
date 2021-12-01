import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { Link } from 'react-router-dom';
import SearchBar from './SearchBar';
import './TopNav.css';
import BasketBar from "./BasketBar";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
title: {
    flexGrow: 1,
    marginRight: theme.spacing(2),
  },
fullName: {
  [theme.breakpoints.down('sm')]: {
    display: 'none',
  },
},
link: {
    color: "inherit",
    textDecoration: "none",
    "&:hover": {
      color: "#d3d3d3"
    }
  },
  basketBar: {
    [theme.breakpoints.down('xs')]: {
      margin: theme.spacing(2),
    }
  },
  searchBar: {
    [theme.breakpoints.down('xs')]: {
      margin: theme.spacing(2),
    }
  },
  flexBreak: {
    display: 'none',
    [theme.breakpoints.down('xs')]: {
      display: 'flex',
      flexBasis: '100%',
      height: '0',
    }
  }  
}));

export default function TopNav(props) {
  const classes = useStyles();

  const { aboutAction, searchAction, showBasketBar } = props;

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" className={classes.title}>
            <Link to="/" className={classes.link}>DELIS<span className={classes.fullName}>{' '}Catalogue</span></Link>
          </Typography>
          <Button color="inherit" onClick={ () => aboutAction() }>About</Button>
          <Link to="/upload" className={classes.link}>
              <Button color="inherit">Upload</Button>
          </Link>
          <div className = {classes.flexBreak} />
          { showBasketBar && (
          <div className = {classes.basketBar}>
            <BasketBar />
          </div>
          )}
          <div className = {classes.searchBar}>
            <SearchBar searchAction = { searchAction } />
          </div>
        </Toolbar>
      </AppBar>
    </div>
  );
}
