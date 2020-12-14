import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import { Button, Card, CardContent, CardHeader, IconButton } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
  card: {
    marginTop: theme.spacing(2),
  },
}));

export default function Banner(props) {
  const { opened, closeAction } = props;

  const classes = useStyles();

  return (
    <>
      {!opened ? (
        <></>
      ) : (
          <Card className={classes.card}>
            <CardHeader
              title="Welcome to DELIS Catalog"
              action={
                <IconButton aria-label="close header" onClick={() => { closeAction() }}>
                  <CloseIcon />
                </IconButton>
              }
            >
            </CardHeader>
            <CardContent style={{ }}>
              <Typography color="textPrimary" variant="body1">
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
              <Button size="small" color="primary" variant="outlined" onClick={() => { closeAction() }}>
                Hide
              </Button>
            </CardContent>
          </Card>
        )}
    </>
  );
}
