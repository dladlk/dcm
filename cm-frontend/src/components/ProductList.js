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
import ProductListHeader from './ProductListHeader';

const useStyles = makeStyles(theme => ({
  table: {
    minWidth: 600,
  },
  header: {
    marginBottom: '1em'
  },
  paper: {
    padding: theme.spacing(2),
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

export default function ProductList(props) {

  const { isLoading, list } = props;

  const classes = useStyles();

  const { push } = useHistory();

  const showRowDetails = (rowId) => {
    console.log('Clicked '+rowId);
    push('/product/view/'+rowId);
  }

  return (
    <>
    <ProductListHeader name="Products"/>
    <Paper className = {classes.paper}>
      {isLoading ? (
        <CircularProgress />
      ) : (
        <TableContainer>
          <Table className={classes.table} size="small" aria-label="Items table">
            <TableHead>
              <TableRow>
                <StyledTableCell align="left">Standard number</StyledTableCell>
                <StyledTableCell align="left">Seller number</StyledTableCell>
                <StyledTableCell align="left">Name</StyledTableCell>
                <StyledTableCell align="left">Certificates</StyledTableCell>
                <StyledTableCell align="left">Categorization</StyledTableCell>
                <StyledTableCell align="left">Origin</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {list?.map(row => (
                <StyledTableRow key={row.id} onClick={() => showRowDetails(row.id)}>
                  <TableCell >{ItemDetailsService.itemStandardNumber(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemSellerNumber(row.document.item)}</TableCell>
                  <TableCell >{row.document.item.name}</TableCell>
                  <TableCell >{ItemDetailsService.itemCertificates(row.document.item).map(cert => (
                    <span key={cert.id}>
                      {cert.id} {cert.certificateType}
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
    </Paper>
    </>
  );
}