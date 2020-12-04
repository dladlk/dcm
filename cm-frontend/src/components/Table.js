import React, { Fragment } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";
import { Link } from "react-router-dom";

const useStyles = makeStyles(theme => ({
  table: {
    minWidth: 600,
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
  },
  link: {
    color: "rgba(0,0,0,0.65)",
    textDecoration: "none",
    "&:hover": {
      color: "rgba(0,0,0,1)"
    }
  }
}));

export default function SimpleTable() {
  const classes = useStyles();

  const [data, updateData] = React.useState([]);
  const [firstLoad, setLoad] = React.useState(true);
  let isLoading = true;

  async function sampleFunc() {
    let response = await fetch("http://localhost:8080/items");
    let body = await response.json();
    updateData(body);
  }

  if (firstLoad) {
    sampleFunc();
    setLoad(false);
  }

  function itemNumber(d) {
    if (d && d.item) {
      return d.item.sellersItemIdentification.id;
    }
    return null;
  }

  if (data.length > 0) isLoading = false;

  return (
    <div className={classes.paper}>
      <Typography component="h1" variant="h5">
        <Link to="/" className={classes.link}>DELIS Catalogue</Link>
      </Typography>

      {isLoading ? (
        <CircularProgress />
      ) : (
        <TableContainer
          style={{ width: "80%", margin: "0 10px" }}
          component={Paper}
        >
          <Table className={classes.table} size="small" aria-label="Items table">
            <TableHead>
              <TableRow>
                <TableCell >Number</TableCell>
                <TableCell >Unit</TableCell>
                <TableCell >Name</TableCell>
                <TableCell align="left">Description</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data?.map(row => (
                <TableRow key={row.id}>
                  <TableCell >
                    {itemNumber(row.document)}
                  </TableCell>
                  <TableCell >{row.document.orderableUnit}</TableCell>
                  <TableCell >{row.document.item.name}</TableCell>
                  <TableCell align="left">
                    {row.document.item.descriptionList?.map((d, i) => (
                      <Fragment key={row.id+'_'+i}>{d}</Fragment>
                    ))}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </div>
  );
}
