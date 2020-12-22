import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { Link } from 'react-router-dom';
import SearchBar from './SearchBar'

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
},
title: {
    flexGrow: 1,
    marginRight: theme.spacing(2),
  },
link: {
    color: "inherit",
    textDecoration: "none",
    "&:hover": {
      color: "#d3d3d3"
    }
  },
}));

export default function TopNav(props) {
  const classes = useStyles();

  const { aboutAction, searchAction } = props;

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" className={classes.title}>
            <Link to="/" className={classes.link}>DELIS Catalogue</Link>
          </Typography>
          <Button color="inherit" onClick={ () => aboutAction() }>About</Button>
          <Link to="/upload" className={classes.link}>
              <Button color="inherit">Upload</Button>
          </Link>
          <Link to="/generate" className={classes.link}>
              <Button color="inherit">Generate</Button>
          </Link>
          <SearchBar searchAction = { searchAction } />
        </Toolbar>
      </AppBar>
    </div>
  );
}
