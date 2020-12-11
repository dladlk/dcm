import React, { Fragment } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import { withStyles } from "@material-ui/core";
import { useHistory } from "react-router";

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
    marginTop: theme.spacing(2)
  }
}));

const StyledTableCell = withStyles((theme) => ({
  head: {
    fontWeight: 'bold',
  },
}))(TableCell);

const StyledTableRow = withStyles((theme) => ({
  root: {
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
    cursor: 'pointer'
  },
}))(TableRow);

export default function SimpleTable() {
  const classes = useStyles();

  const [data, updateData] = React.useState(null);
  const [firstLoad, setLoad] = React.useState(true);
  const { push } = useHistory();
  let isLoading = true;

  async function loadProducts() {
    let response = await fetch("http://localhost:8080/products");
    let body = await response.json();
    updateData(body);
  }

  if (firstLoad) {
    loadProducts();
    setLoad(false);
  }
  function itemOriginCountry(item) {
    if (item && item.originCountry) {
      return item.originCountry.identificationCode;
    }
    return null;
  }
  function itemUNSPSC(item) {
    if (item && item.commodityClassificationList) {
      if (item.commodityClassificationList.length > 0) {
        let code = item.commodityClassificationList[0];
        if (code && code.itemClassificationCode) {
          return code.itemClassificationCode.value;
        }
      }
    }
    return null;
  }
  function itemSellerNumber(item) {
    if (item) {
      if (item.sellersItemIdentification) {
        return item.sellersItemIdentification.id;
      }
    }
    return null;
  }
  function itemStandardNumber(item) {
    if (item) {
      if (item.standardItemIdentification && item.standardItemIdentification.id) {
        return item.standardItemIdentification.id.id;
      }
    }
    return null;
  }

  const showRowDetails = (rowId) => {
    console.log('Clicked '+rowId);
    push('/product/view/'+rowId);
  }

  if (data != null) isLoading = false;

  return (
    <div className={classes.paper}>
      {isLoading ? (
        <CircularProgress />
      ) : (
        <TableContainer
          component={Paper}
        >
          <Table className={classes.table} size="small" aria-label="Items table">
            <TableHead>
              <TableRow>
                <StyledTableCell align="center">Seller number</StyledTableCell>
                <StyledTableCell align="center">Standard number</StyledTableCell>
                <StyledTableCell align="center">Unit</StyledTableCell>
                <StyledTableCell align="left">Name</StyledTableCell>
                <StyledTableCell align="left">Description</StyledTableCell>
                <StyledTableCell align="left">UNSPSC</StyledTableCell>
                <StyledTableCell align="left">Country</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data?.map(row => (
                <StyledTableRow key={row.id} onClick={() => showRowDetails(row.id)}>
                  <TableCell >{itemSellerNumber(row.document.item)}</TableCell>
                  <TableCell >{itemStandardNumber(row.document.item)}</TableCell>
                  <TableCell >{row.document.orderableUnit}</TableCell>
                  <TableCell >{row.document.item.name}</TableCell>
                  <TableCell align="left">
                    {row.document.item.descriptionList?.map((d, i) => (
                      <Fragment key={row.id+'_'+i}>{d}</Fragment>
                    ))}
                  </TableCell>
                  <TableCell >{itemUNSPSC(row.document.item)}</TableCell>
                  <TableCell >{itemOriginCountry(row.document.item)}</TableCell>
                </StyledTableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
}
