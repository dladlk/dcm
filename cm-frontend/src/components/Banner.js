import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import { Card, CardContent, CardHeader, IconButton } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  card: {
    marginTop: theme.spacing(2),
  },
}));

export default function Banner(props) {
  const {open} = props;
  const [hide, setHide] = React.useState(false);

  const classes = useStyles();

  return (
      <>
      {hide ? (
        <></>
      ) : (
      <Card className = {classes.card}>
        <CardHeader
          title = "Welcome to DELIS Catalog"
          action = {
          <IconButton aria-label="close header" onClick={() => { setHide(true)}}>
            <CloseIcon />
          </IconButton>            
          }
          >
        </CardHeader>
        <CardContent>
          <Typography color="textPrimary" variant="body">
            Here you can search for published products, delivered via Peppol in Catalogue transaction 3.1 format by different sellers, and find aggregated information by standard identifier for next concerns:
            <ul>
              <li>product descriptions</li>
              <li>classification</li>
              <li>environmental labelling</li>
              <li>social responsibility</li>
              <li>ecological labelling</li>
              <li>pictures</li>
              <li>country of origin</li>
            </ul>
          </Typography>
        </CardContent>
      </Card>
      )}
      </>
  );
}
