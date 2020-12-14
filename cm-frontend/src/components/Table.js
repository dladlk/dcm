import React from "react";
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
import ItemDetailsService from "../services/ItemDetailsService";


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
    '&:hover': {
      backgroundColor: 'rgba(0,0,0,.08)',
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
                <StyledTableCell align="left">Standard number</StyledTableCell>
                <StyledTableCell align="left">Seller number</StyledTableCell>
                <StyledTableCell align="left">Name</StyledTableCell>
                <StyledTableCell align="left">Certificates</StyledTableCell>
                <StyledTableCell align="left">UNSPSC</StyledTableCell>
                <StyledTableCell align="left">Origin</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data?.map(row => (
                <StyledTableRow key={row.id} onClick={() => showRowDetails(row.id)}>
                  <TableCell >{ItemDetailsService.itemStandardNumber(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemSellerNumber(row.document.item)}</TableCell>
                  <TableCell >{row.document.item.name}</TableCell>
                  <TableCell >{ItemDetailsService.itemCertificates(row.document.item).map(cert => (
                    <span key={cert.id}>
                    {cert.certificateType} {cert.issuerParty.partyName.name}
                    </span>
                  ))}</TableCell>
                  <TableCell >{ItemDetailsService.itemUNSPSC(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemOriginCountry(row.document.item)}</TableCell>
                </StyledTableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
}
