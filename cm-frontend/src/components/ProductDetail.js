import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { useParams } from "react-router";
import ProductView from "./ProductView";
import DetailHeader from "./DetailHeader";
import CircularProgress from "@material-ui/core/CircularProgress";
import { FormControl, FormControlLabel, FormLabel, Paper, Radio, RadioGroup } from "@material-ui/core";
import DataService from "../services/DataService";

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

  const [data, setData] = React.useState(null);
  const [dataLoading, setDataLoading] = React.useState(true);
  const [viewMode, setViewMode] = React.useState("table");

  React.useEffect(() => {
    loadProduct();
  }, []);

  async function loadProduct() {
    setDataLoading(true);
    let response = await DataService.fetchProductDetails(id);
    let body = await response.json();
    setData(body);
    setDataLoading(false);
  }

  const handleViewChange = (event) => {
    setViewMode(event.target.value);
  };  

  return (
    <>
    <DetailHeader name="Product details" />
    <Paper className={classes.paper}>
      {dataLoading ? (
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
    </>
  );
}
