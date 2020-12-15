import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import axios from "axios";
import { Box, Button, Paper, Snackbar } from "@material-ui/core";
import { DropzoneArea } from 'material-ui-dropzone'

const useStyles = makeStyles(theme => ({
  paper: {
    marginTop: theme.spacing(3),
    padding: theme.spacing(4),
    width: '50%',
    minWidth: '400px',
  },
  previewChip: {
  }
}));

export default function Upload() {
  const classes = useStyles();

  const [snakBarOpen, setSnakBarOpen] = React.useState(false);
  const [selectedFiles, setSelectedFiles] = React.useState([]);
  const [dropzoneKey, setDropzoneKey] = React.useState(0);


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

  function handleUpload() {
    if (selectedFiles && selectedFiles.length > 0) {
      uploadFile(selectedFiles);
    }
  }

  function handleChange(files) {
    setSelectedFiles(files);
  }

  function handleClear() {
    setSelectedFiles([]);
    setDropzoneKey(dropzoneKey + 1);
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
          key = {dropzoneKey}
          onChange={handleChange}
          acceptedFiles={['application/xml', 'text/xml']}
          filesLimit={10}
          showPreviewsInDropzone={true}
          useChipsForPreview
          previewGridProps={{ container: { spacing: 1, direction: 'row' } }}
          previewChipProps={{ classes: { root: classes.previewChip } }}
          previewText="Selected files"
          showAlerts={false}
          dropzoneText={"Drag and drop a file here or click icon"}
        />

        <Box m={3} display="flex" justifyContent="center">
          <Button variant="contained" color="primary" onClick = {() => handleUpload()} disabled = { selectedFiles.length == 0 } >Upload</Button>
          <Box pl={1}>
            <Button variant="contained" onClick = {() => handleClear()} disabled = { selectedFiles.length == 0 }>Clear</Button>
          </Box>
        </Box>

      </Paper>
      <Snackbar open={snakBarOpen} autoHideDuration={1000} onClose={handleSnakBarClose}>
        <Typography component="h6">
          File is successfully uploaded
        </Typography>
      </Snackbar>
    </Box>
  );
}
