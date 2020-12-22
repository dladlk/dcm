import { Grid, Input, makeStyles, Paper } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
    root: {
      flexGrow: 1,
      marginTop: theme.spacing(2),
    },
    paper: {
      padding: theme.spacing(0.1),
      margin: theme.spacing(0.1),
      textAlign: 'center',
      color: theme.palette.text.secondary,
    },
  }));

export default function GenerateCatalogPage(props) {
    
    const _defaultCatalog = {
        id: '1',
        name: 'Test catalogue',
        version: '1',
        action: 'Update',
        date: '2020-12-21',

        issuerParty: {
            endpointId: '968993038',
            id: '968993038',
            name: 'Issuer',
            number: '',
            zip: '2016'
        }, 
        receiverParty: {
            endpointId: '968993038',
            id: '968993038',
            name: 'Receiver',
            number: '',
            zip: '2016'
        },
    };

    const { catalog = _defaultCatalog } = props;

    const classes=useStyles();

    return (
        <div className={classes.root}>
            <Grid container spacing={0.1}>
                <Grid item lg={2}><Paper className={classes.paper} style = {{ fontWeight: "bold"}}>Catalog header</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Number</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Name</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Version</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Action</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Date</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Valid from</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Valid to</Paper></Grid>
                <Grid item lg={3}></Grid>

                <Grid item lg={2}></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.id} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.name} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.version} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.action} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.date} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} /></Grid>
                <Grid item lg={3}></Grid>

                <Grid item lg={2}><Paper className={classes.paper} style = {{ fontWeight: "bold"}}>Parties</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>EndpointID</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>ID</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Name</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>Number</Paper></Grid>
                <Grid item lg={1}><Paper className={classes.paper}>ZIP</Paper></Grid>
                <Grid item lg={5}></Grid>

                <Grid item lg={2}>Issuer</Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.issuerParty.endpointId} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.issuerParty.id} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.issuerParty.name} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.issuerParty.number} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.issuerParty.zip} /></Grid>
                <Grid item lg={5}></Grid>                

                <Grid item lg={2}>Receiver</Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.receiverParty.endpointId} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.receiverParty.id} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.receiverParty.name} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.receiverParty.number} /></Grid>
                <Grid item lg={1}><Input className={classes.paper} value={catalog.receiverParty.zip} /></Grid>
                <Grid item lg={5}></Grid>                
            </Grid>
        </div>
    )
}