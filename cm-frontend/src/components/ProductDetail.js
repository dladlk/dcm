import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { useParams } from "react-router";
import ProductView from "./ProductView";
import CircularProgress from "@material-ui/core/CircularProgress";
import { FormControl, FormControlLabel, FormLabel, Paper, Radio, RadioGroup } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
  paper: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "left",
    alignItems: "left",
    height: "100%",
    padding: theme.spacing(2),
    marginTop: theme.spacing(3),
  }
}));

export default function ProductDetail() {
  
  let { id } = useParams();

  const classes = useStyles();

  const [data, updateData] = React.useState(null);
  const [firstLoad, setLoad] = React.useState(true);
  const [viewMode, setViewMode] = React.useState("table");

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

  const handleViewChange = (event) => {
    setViewMode(event.target.value);
  };  

  if (data != null) isLoading = false;

  return (
    <Paper className={classes.paper}>
      {isLoading ? (
        <CircularProgress />
      ) : (

        <>
        <FormControl component="fieldset">
          <FormLabel>View</FormLabel>
          <RadioGroup row aria-label="view" name="view" value={viewMode} onChange={handleViewChange}>
            <FormControlLabel value="table" control={<Radio />} label="Table" />
            <FormControlLabel value="json" control={<Radio />} label="JSON" />
          </RadioGroup>
        </FormControl>

        {viewMode === "json" ? (
          <pre>{JSON.stringify(data, null, 2)}</pre>
        ) : (
          <ProductView product={data}></ProductView>
        )
        }
        </>
      )}
    </Paper>
  );
}
