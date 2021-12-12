import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import {useParams} from "react-router";
import ProductDetail from "../components/ProductDetail";
import ProductDetailHeader from "../components/ProductDetailHeader";
import CircularProgress from "@material-ui/core/CircularProgress";
import {Box, FormControl, FormControlLabel, Paper, Radio, RadioGroup} from "@material-ui/core";
import DataService from "../services/DataService";
import MergeService from "../services/MergeService";

const useStyles = makeStyles(theme => ({
    paper: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "left",
        alignItems: "left",
        height: "100%",
        padding: theme.spacing(2),
        marginBottom: theme.spacing(3),
    }
}));

function ViewToggle(props) {
    return (
        <Box display="flex" flex="1" justifyContent="flex-end">
            <FormControl component="fieldset">
                <RadioGroup row aria-label="view" name="view" value={props.viewMode} onChange={props.handleViewChange}>
                    <FormControlLabel value="table" control={<Radio/>} label="Table"/>
                    <FormControlLabel value="json" control={<Radio/>} label="JSON"/>
                </RadioGroup>
            </FormControl>
        </Box>
    )
}

export default function ProductDetailPage(props) {

    const {navigator} = props;

    let {id} = useParams();

    const classes = useStyles();

    const [data, setData] = React.useState(null);
    const [dataLoading, setDataLoading] = React.useState(true);
    const [viewMode, setViewMode] = React.useState("table");

    React.useEffect(() => {
        loadProduct(id).finally(() => setDataLoading(false));
    }, [id]);

    async function loadProduct(id) {
        setDataLoading(true);
        await DataService.fetchProductDetails(id).then(response => {
            let res = response.data;
            if (Array.isArray(res)) {
                res = MergeService.mergeProducts(res);
            }
            setData(res);
        }).catch((error) => {
            setData(null);
            console.log('Error occurred: ' + error.message);
            // setErrorMessage(error.message);
        });
    }

    const handleViewChange = (event) => {
        setViewMode(event.target.value);
    };

    return (
        <>
            <ProductDetailHeader name="Product details" navigator={navigator} id={id} refreshAction={loadProduct} {...props} product={data}/>

            <ViewToggle viewMode={viewMode} handleViewChange={handleViewChange}/>

            <Paper className={classes.paper}>
                {dataLoading ? (
                    <CircularProgress/>
                ) : (
                    <>
                        {data !== null ? (
                            <>
                                {viewMode === "json" ? (
                                    <pre style={{whiteSpace: 'pre-wrap', wordBreak: 'break-all'}}>{JSON.stringify(data, null, 2)}</pre>
                                ) : (
                                    <ProductDetail product={data} {...props} />
                                )
                                }
                            </>
                        ) : (
                            <div>Product not found</div>
                        )}
                    </>
                )}
            </Paper>
        </>
    );
}
