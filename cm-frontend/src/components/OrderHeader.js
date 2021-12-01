import {Grid, Paper, TextField, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

export default function OrderHeader() {

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
                <DataInput label="Registration name" defaultValue="My Company ApS" required/>
                <DataInput label="Legal identifier" defaultValue="DK11223344" required/>
                <DataInput label="Party identifier" defaultValue="7300010000001" required/>
            </DataBlock>
            <DataBlock name={"Buyer contact"}>
                <DataInput label="Person name" defaultValue="John Dohn"/>
                <DataInput label="Email" defaultValue="unexisting@email.com"/>
                <DataInput label="Telephone" defaultValue="+45 11223344"/>
            </DataBlock>
        </Grid>
    </Paper>
}