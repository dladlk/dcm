import React from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import TablePagination from "@material-ui/core/TablePagination";
import { useHistory } from "react-router";
import ItemDetailsService from "../services/ItemDetailsService";
import ProductListHeader from '../components/ProductListHeader';

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

export default function ProductListPage(props) {

  const { isLoading, list, refreshAction, page, pageSize, total } = props;

  const classes = useStyles();

  const { push } = useHistory();

  const showRowDetails = (rowId) => {
    console.log('Clicked '+rowId);
    push('/product/view/'+rowId);
  }

  const handleChangePage = (e, newPage)=> {
    console.log('handleChangePage to '+newPage);
    refreshAction(null, newPage, pageSize);
  };
  const handleChangeRowsPerPage = (e)=> {
    console.log('handleChangeRowsPerPage to '+e.target.value);
    refreshAction(null, page, e.target.value);
  };

  return (
    <>
    <ProductListHeader name="Products" refreshAction = {refreshAction}/>
    <Paper className = {classes.paper}>
      {isLoading ? (
        <CircularProgress />
      ) : (
        <>
        <TableContainer>
          <Table className={classes.table} size="small" aria-label="Items table">
            <TableHead>
              <TableRow>
                <StyledTableCell align="left">Name</StyledTableCell>
                <StyledTableCell align="left">Standard number</StyledTableCell>
                <StyledTableCell align="left">Seller number</StyledTableCell>
                <StyledTableCell align="left">Certificates</StyledTableCell>
                <StyledTableCell align="left">Categorization</StyledTableCell>
                <StyledTableCell align="left">Origin</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {(list && list.length > 0)? list?.map(row => (
                <StyledTableRow key={row.id} onClick={() => showRowDetails(row.id)}>
                  <TableCell >{row.document.item.name}</TableCell>
                  <TableCell >{ItemDetailsService.itemStandardNumber(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemSellerNumber(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemCertificates(row.document.item).map(ItemDetailsService.renderItemCertificate)}</TableCell>
                  <TableCell >{ItemDetailsService.itemUNSPSC(row.document.item)}</TableCell>
                  <TableCell >{ItemDetailsService.itemOriginCountry(row.document.item)}</TableCell>
                </StyledTableRow>
              )) : (
                <StyledTableRow key={'empty'}>
                  <TableCell colSpan={6} align="center">No items are found</TableCell>
                </StyledTableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        
        {list && (
        <TablePagination
          rowsPerPageOptions={[5, 20, 100]}
          component="div"
          count={total}
          rowsPerPage={pageSize}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
          labelRowsPerPage={'Page size: '}
        />
        )}

        </>
      )}
    </Paper>
    </>
  );
}
