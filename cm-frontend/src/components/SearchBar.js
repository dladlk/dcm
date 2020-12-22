import React, { useRef } from 'react';
import { fade, makeStyles } from '@material-ui/core/styles';
import SearchIcon from '@material-ui/icons/Search';
import ClearIcon from '@material-ui/icons/Clear';
import InputBase from '@material-ui/core/InputBase';
import { IconButton } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
    search: {
      position: 'relative',
      borderRadius: theme.shape.borderRadius,
      backgroundColor: fade(theme.palette.common.white, 0.15),
      '&:hover': {
        backgroundColor: fade(theme.palette.common.white, 0.25),
      },
      marginLeft: 0,
      width: '100%',
      [theme.breakpoints.up('sm')]: {
        marginLeft: theme.spacing(1),
        width: 'auto',
      },
    },
    searchIcon: {
      padding: theme.spacing(0, 2),
      height: '100%',
      position: 'absolute',
      pointerEvents: 'none',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
    },
    inputRoot: {
      color: 'inherit',
    },
    inputInput: {
      padding: theme.spacing(1, 1, 1, 0),
      // vertical padding + font size from searchIcon
      paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
      transition: theme.transitions.create('width'),
      width: '100%',
      [theme.breakpoints.up('md')]: {
        width: '15ch',
        '&:focus': {
          width: '30ch',
        },
      },
    },
  
  }));

export default function SearchBar(props) {
    const classes = useStyles();

    const inputEl = useRef(null);

    return (

        <div className={classes.search}>
        <div className={classes.searchIcon}>
          <SearchIcon />
        </div>
        <InputBase
          inputRef = {inputEl}
          placeholder="Search for products"
          classes={{
            root: classes.inputRoot,
            input: classes.inputInput,
          }}
          inputProps={{ 
            'aria-label': 'search' ,
          }}
          endAdornment = {(
            <IconButton onClick={() => {inputEl.current.value = ''; props.searchAction('');}} className = {classes.inputRoot} >
              <ClearIcon />
            </IconButton>              
            )}
          onKeyPress = { (e) => e.key === 'Enter' && props.searchAction(e.target.value)}
          />
      </div>          

    )
}