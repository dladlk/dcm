import React from "react";
import clsx from 'clsx';
import { makeStyles } from "@material-ui/core/styles";
import { Box, Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@material-ui/core";
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
    minWidth: '300px',
  },
  previewChip: {
  },

  buttonSuccess: {
    backgroundColor: theme.palette.success.main,
  },
  buttonLoading: {
    backgroundColor: theme.palette.success.light,
  },
  progressWrapper: {
    margin: theme.spacing(0),
    position: 'relative',
  },  
  fabProgress: {
    color: theme.palette.success.main,
    position: 'absolute',
    top: -3,
    left: -3,
    zIndex: 1,
  },
  buttonProgress: {
    color: theme.palette.success.main,
    position: 'absolute',
    top: '50%',
    left: '50%',
    marginTop: -12,
    marginLeft: -12,
    zIndex: 1,
  },
}));

export default function Upload() {
  const classes = useStyles();
  const [selectedFiles, setSelectedFiles] = React.useState([]);
  const [uploadResultList, setUploadResultList] = React.useState([]);
  const [dropzoneKey, setDropzoneKey] = React.useState(0);
  const [dropzoneDisabled, setDropzoneDisabled] = React.useState(false);
  const [success, setSuccess] = React.useState(false);
  const [loading, setLoading] = React.useState(false);

  const buttonClassname = clsx({
    [classes.buttonSuccess]: success,
    [classes.buttonLoading]: loading,
  });  

  const history = useHistory();

  function uploadFile(files) {
    setDropzoneDisabled(true);
    setSuccess(false);
    setLoading(true);
    setUploadResultList([]);
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
        setUploadResultList(res.data);
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
    if (loading) {
      return;
    }
    if (selectedFiles && selectedFiles.length > 0) {
      uploadFile(selectedFiles);
    }
  }

  function handleChange(files) {
    setSelectedFiles(files);
    setSuccess(false);
  }

  function isEmpty() {
    return selectedFiles.length === 0;
  }

  function handleClear() {
    setSuccess(false);
    setSelectedFiles([]);
    setUploadResultList([]);
    setDropzoneKey(dropzoneKey + 1);
  }

  function handleBack() {
    history.push('/');
  }

  return (
    <>
      <Box display="flex" justifyContent="center" mb={3}>
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
            <div className={classes.progressWrapper}>
              <Fab
                size="small"
                aria-label="save"
                color="primary"
                style={{ marginRight: '10px', width: '35px', height: '35px' }}
                className={buttonClassname}
                onClick={() => handleUpload()} disabled={isEmpty()}
              >
                {success ? <CheckIcon /> : <SaveIcon />}
              </Fab>
              {loading && <CircularProgress size={41} thickness="3" className={classes.fabProgress} />}
            </div>
            <div className={classes.progressWrapper}>
            <Button variant="contained" color="primary" className={buttonClassname} onClick={() => handleUpload()} disabled={isEmpty()} >Upload</Button>
            {loading && <CircularProgress size={24} className={classes.buttonProgress} />}
            </div>
            <Box pl={1}>
              <Button variant="contained" onClick={() => handleClear()} disabled={isEmpty()}>Clear</Button>
            </Box>
            <Box pl={1}>
              <Button variant="contained" onClick={() => handleBack()}>Back</Button>
            </Box>
          </Box>
        </Paper>
      </Box>

      {uploadResultList?.length > 0 && (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableCell>File name</TableCell>
              <TableCell>Version</TableCell>
              <TableCell>Action</TableCell>
              <TableCell>ID</TableCell>
              <TableCell>Success</TableCell>
              <TableCell>Error message</TableCell>
              <TableCell>Line count</TableCell>
              <TableCell>Add</TableCell>
              <TableCell>Update</TableCell>
              <TableCell>Delete</TableCell>
            </TableHead>
            <TableBody>
              {uploadResultList?.map((u) => {
                return (
                  <TableRow key={u.fileName}>
                    <TableCell component="th" scope="row">{u.fileName}</TableCell>
                    <TableCell>{u.productCatalogUpdate?.documentVersion}</TableCell>
                    <TableCell>{u.productCatalogUpdate?.document?.actionCode}</TableCell>
                    <TableCell>{u.productCatalogUpdate?.document?.id}</TableCell>
                    <TableCell>{u.success}</TableCell>
                    <TableCell>{u.errorMessage}</TableCell>
                    <TableCell>{u.lineCount}</TableCell>
                    <TableCell>{u.lineActionStat.ADD}</TableCell>
                    <TableCell>{u.lineActionStat.UPDATE}</TableCell>
                    <TableCell>{u.lineActionStat.DELETE}</TableCell>
                  </TableRow>
                )
              })}

            </TableBody>
          </Table>
        </TableContainer>
      )}

    </>
  );
}
