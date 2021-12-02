import {Grid, Paper, TextField, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

export default function OrderHeader(props) {

    const {orderData: orderData} = props;

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
        formHeader: {
            paddingTop: theme.spacing(1),
            paddingLeft: theme.spacing(2),
            textAlign: "left",
            fontSize: '1em',
        },
        form: {
            padding: theme.spacing(2),
            display: "flex",
            flex: "1",
            flexDirection: "row",
            justifyContent: "space-between",
        },
        input: {
            paddingInline: theme.spacing(0.5),
        }
    }));

    const classes = useStyles();

    function DataInput(props) {
        return <TextField className={classes.input} size={"small"} {...props} />
    }

    function DataBlock(props) {
        return <Grid item sm={12} md={6}>
            <Paper>
                <div className={classes.formHeader}><Typography variant="h6">{props.name}</Typography></div>
                <form className={classes.form} noValidate autoComplete="on">
                    {props.children}
                </form>
            </Paper>
        </Grid>

    }

    return <Paper className={classes.paper}>
        <Grid container spacing={2}>
            <DataBlock name={"Buyer company"}>
                <DataInput label="Registration name" required value={orderData.buyerCompany.registrationName}/>
                <DataInput label="Legal identifier" required value={orderData.buyerCompany.legalIdentifier}/>
                <DataInput label="Party identifier" required value={orderData.buyerCompany.partyIdentifier}/>
            </DataBlock>
            <DataBlock name={"Buyer contact"}>
                <DataInput label="Person name" value={orderData.buyerContact.personName}/>
                <DataInput label="Email" value={orderData.buyerContact.email}/>
                <DataInput label="Telephone" value={orderData.buyerContact.telephone}/>
            </DataBlock>
        </Grid>
    </Paper>
}