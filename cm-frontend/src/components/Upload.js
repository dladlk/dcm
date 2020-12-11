import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import axios from "axios";
import { Snackbar } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
  table: {
    minWidth: 600,
  },
  header: {
    marginBottom: '1em'
  },
  paper: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    margin: "10px",
    height: "100%",
    width: "99%",
    marginTop: theme.spacing(7)
  }
}));

export default function Upload() {
  const classes = useStyles();

  const [selectedFile, setSelectedFile] = React.useState(null);
  const [snakBarOpen, setSnakBarOpen] = React.useState(false);

  function onFileChange(event) {
    setSelectedFile(event.target.files[0]); 
  }
  function onFileUpload() { 
    const formData = new FormData(); 
    console.log(selectedFile); 
    formData.append( 
      "file", 
      selectedFile, 
      selectedFile.name 
    ); 
    axios.post("http://localhost:8080/upload", formData).then((res) => {
      console.log(res);
      setSnakBarOpen(true);
    });
  };

  function handleSnakBarClose(event, reason) {
    if (reason === 'clickaway') {
      return;
    }
    setSnakBarOpen(false);
  }

  return (
    <div className={classes.paper}>
      <div>
          <input type="file" onChange={onFileChange}/><button onClick={onFileUpload}>Upload</button>
      </div>

      <Snackbar open={snakBarOpen} autoHideDuration={1000} onClose={handleSnakBarClose}>
        <Typography component="h6">
          File is successfully uploaded
        </Typography>
      </Snackbar>

    </div>
  );
}
