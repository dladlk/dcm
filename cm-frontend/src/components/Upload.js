import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import axios from "axios";
import { Box, Paper, Snackbar } from "@material-ui/core";
import {DropzoneArea} from 'material-ui-dropzone'

const useStyles = makeStyles(theme => ({
  paper: {
    marginTop: theme.spacing(3),
    padding: theme.spacing(4),
    width: '50%',
    minWidth: '400px',
  }
}));

export default function Upload() {
  const classes = useStyles();

  const [snakBarOpen, setSnakBarOpen] = React.useState(false);

  function uploadFile(files) { 
    const formData = new FormData(); 
    for (const file of files) {
      let selectedFile = file;
      console.log(selectedFile); 
      formData.append( 
        "files", 
        selectedFile, 
        selectedFile.name 
      ); 
    }
    axios.post("http://localhost:8080/upload", formData).then((res) => {
      console.log(res);
      setSnakBarOpen(true);
    });
  };

  function handleChange(files) {
    if (files && files.length > 0) {
      uploadFile(files);
    }
  }  

  function handleSnakBarClose(event, reason) {
    if (reason === 'clickaway') {
      return;
    }
    setSnakBarOpen(false);
  }

  return (
    <Box display="flex" justifyContent="center">
    <Paper className={classes.paper}>
      <DropzoneArea
        onChange={handleChange}
        acceptedFiles={['application/xml', 'text/xml']}
        filesLimit = {10}
        showPreviewsInDropzone = {true}
        showAlerts = {false}
        dropzoneText = {"Drag and drop a file here or click icon"}
        />

      <Snackbar open={snakBarOpen} autoHideDuration={1000} onClose={handleSnakBarClose}>
        <Typography component="h6">
          File is successfully uploaded
        </Typography>
      </Snackbar>
    </Paper>
    </Box>
  );
}
