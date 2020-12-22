import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { useParams } from "react-router";
import ProductView from "../components/ProductView";
import DetailHeader from "../components/DetailHeader";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Box, FormControl, FormControlLabel, Paper, Radio, RadioGroup } from "@material-ui/core";
import DataService from "../services/DataService";

const useStyles = makeStyles(theme => ({
  paper: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "left",
    alignItems: "left",
    height: "100%",
    padding: theme.spacing(2),
  }
}));

function ViewToggle(props) {
  return (
    <Box display="flex" flex="1" justifyContent="flex-end">
      <FormControl component="fieldset">
          <RadioGroup row aria-label="view" name="view" value={props.viewMode} onChange={props.handleViewChange}>
            <FormControlLabel value="table" control={<Radio />} label="Table" />
            <FormControlLabel value="json" control={<Radio />} label="JSON" />
          </RadioGroup>
        </FormControl>
    </Box>    
  )
}

export default function ProductDetailPage(props) {
  
  const { navigator } = props;

  let { id } = useParams();

  const classes = useStyles();

  const [data, setData] = React.useState(null);
  const [dataLoading, setDataLoading] = React.useState(true);
  const [viewMode, setViewMode] = React.useState("table");

  React.useEffect(() => {
    loadProduct(id);
  }, [id]);

  async function loadProduct(id) {
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
    <DetailHeader name="Product details" navigator = { navigator } id = { id } />

    <ViewToggle viewMode={viewMode} handleViewChange={handleViewChange}/>

    <Paper className={classes.paper}>
      {dataLoading ? (
        <CircularProgress />
      ) : (
        <>
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