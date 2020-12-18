import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import { Box, Button, Card, CardContent, CardHeader, IconButton } from '@material-ui/core';

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
            <CardContent>
                <div>
                  Here you can search for published products, delivered via Peppol in Catalogue transaction 3.1 format by different sellers,
                  and find aggregated information by standard identifiers.
                </div>
              <Box display="flex" justifyContent="left" mt={3}>
                <Box flex="1"  maxWidth = "500px">
                  Aggregated information includes master data about:
                    <ul>
                    <li>product descriptions</li>
                    <li>classification</li>
                    <li>environmental and ecological labelling</li>
                    <li>social responsibility</li>
                    <li>pictures</li>
                    <li>country of origin</li>
                  </ul>
                </Box>
                <Box flex="1" maxWidth = "500px">
                  Solution includes only master data, and does not include sellers-specific information about:
                    <ul>
                    <li>prices</li>
                    <li>content and orderable units</li>
                    <li>related items</li>
                  </ul>
                </Box>
              </Box>
              <Button size="small" color="primary" variant="outlined" onClick={() => { closeAction() }}>
                Close
                </Button>
            </CardContent>
          </Card>
        )}
    </>
  );
}
