import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { useParams } from "react-router";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Paper } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
  paper: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    height: "100%",
    width: "99%",
    marginTop: theme.spacing(7)
  }
}));

export default function ProductDetail() {
  let { id } = useParams();

  const classes = useStyles();

  const [data, updateData] = React.useState(null);
  const [firstLoad, setLoad] = React.useState(true);

  let isLoading = true;

  async function loadProduct() {
    let response = await fetch("http://localhost:8080/product/"+id);
    let body = await response.json();
    updateData(body);
  }

  if (firstLoad) {
    loadProduct();
    setLoad(false);
  }

  if (data != null) isLoading = false;

  return (
    <Paper className={classes.paper}>
      {isLoading ? (
        <CircularProgress />
      ) : (
        <pre>{JSON.stringify(data, null, 2)}</pre>
      )}
    </Paper>
  );
}
