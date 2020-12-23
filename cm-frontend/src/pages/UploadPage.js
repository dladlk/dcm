import React from "react";
import clsx from 'clsx';
import { makeStyles } from "@material-ui/core/styles";
import { Box, Button, Paper } from "@material-ui/core";
import CircularProgress from '@material-ui/core/CircularProgress';
import Fab from '@material-ui/core/Fab';
import CheckIcon from '@material-ui/icons/Check';
import SaveIcon from '@material-ui/icons/Save';

import { DropzoneArea } from 'material-ui-dropzone'
import DataService from "../services/DataService";
import { useHistory } from "react-router";

const useStyles = makeStyles(theme => ({
  paper: {
    marginTop: theme.spacing(3),
    padding: theme.spacing(4),
    width: '50%',
    minWidth: '480px',
  },
  previewChip: {
  },
  buttonSuccess: {
    backgroundColor: theme.palette.success.main,
  },
  fabProgress: {
    color: theme.palette.success.main,
    position: 'absolute',
    top: -6,
    left: -6,
    zIndex: 1,
  },
  buttonProgress: {
    color: theme.palette.success.main,
    position: 'absolute',
    top: '50%',
    left: '50%',
    marginTop: -12,
    marginLeft: -12,
  },
}));

export default function Upload() {
  const classes = useStyles();

  const [selectedFiles, setSelectedFiles] = React.useState([]);
  const [dropzoneKey, setDropzoneKey] = React.useState(0);
  const [dropzoneDisabled, setDropzoneDisabled] = React.useState(false);
  const [success, setSuccess] = React.useState(false);
  const [loading, setLoading] = React.useState(false);

  const history = useHistory();

  function uploadFile(files) {
    setDropzoneDisabled(true);
    setSuccess(false);
    setLoading(true);    
    const formData = new FormData();
    for (const file of files) {
      let selectedFile = file;
      formData.append(
        "files",
        selectedFile,
        selectedFile.name
      );
    }
    DataService.uploadFiles(formData)
      .then((res) => {
        console.log(res);
        setDropzoneDisabled(false);
        setSuccess(true);
        setLoading(false);        
      })
      .catch((e) => {
        console.log(e);
        setDropzoneDisabled(false);
        setSuccess(false);
        setLoading(false);        
      }
      )
  };

  function handleUpload() {
    if (selectedFiles && selectedFiles.length > 0) {
      uploadFile(selectedFiles);
    }
  }

  function handleChange(files) {
    setSelectedFiles(files);
  }

  function isEmpty() {
    return selectedFiles.length === 0;
  }

  function handleClear() {
    setSuccess(false);
    setSelectedFiles([]);
    setDropzoneKey(dropzoneKey + 1);
  }

  function handleBack() {
    history.push('/');
  }

  const buttonClassname = clsx({
    [classes.buttonSuccess]: success,
  });

  return (
    <Box display="flex" justifyContent="center">
      <Paper className={classes.paper}>
        <DropzoneArea
          disabled={dropzoneDisabled}
          key={dropzoneKey}
          clearOnUnmount={false}
          onChange={handleChange}
          acceptedFiles={['application/xml', 'text/xml']}
          filesLimit={10}
          showPreviewsInDropzone={true}
          useChipsForPreview
          previewGridProps={{ container: { spacing: 1, direction: 'row' } }}
          previewChipProps={{ classes: { root: classes.previewChip } }}
          previewText="Selected files"
          showAlerts={false}
          dropzoneText={"Drag and drop XML files with Peppol Catalogue or click icon"}
        />

        <Box m={3} display="flex" justifyContent="center">
          <div className={classes.wrapper}>
            <Fab
              size="small"
              aria-label="save"
              color="primary"
              style = {{marginRight: '10px', width: '35px', height: '35px'}}
              className={buttonClassname}
              onClick={() => handleUpload()} disabled={isEmpty()}
            >
              {success ? <CheckIcon /> : <SaveIcon />}
            </Fab>
            {loading && <CircularProgress size={68} className={classes.fabProgress} />}
          </div>
          <Button variant="contained" color="primary" className={buttonClassname} onClick={() => handleUpload()} disabled={isEmpty()} >Upload</Button>
          <Box pl={1}>
            <Button variant="contained" onClick={() => handleClear()} disabled={isEmpty()}>Clear</Button>
          </Box>
          <Box pl={1}>
            <Button variant="contained" onClick={() => handleBack()}>Back</Button>
          </Box>
        </Box>

      </Paper>

    </Box>
  );
}
