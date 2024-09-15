import React from "react";
import {Grid, Paper, TextField, Typography} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

const buildDefaultLabelByName = (str) => {
    return str.split(/(?=[A-Z])/).map((s) => s.charAt(0).toUpperCase() + s.slice(1)).join(" ")
}

const DataInput = (props) => {
    const {name, label = buildDefaultLabelByName(name), lockControls, target, ...rest} = props;

    const [value, setValue] = React.useState(target[name]);

    const onChange = (e) => {
        const newValue = e.target.value;
        target[e.target.name] = newValue;
        return setValue(newValue);
    };

    const useStyles = makeStyles((theme) => ({
        input: {
            paddingInline: theme.spacing(0.5),
        }
    }));

    const classes = useStyles();

    return <TextField name={name} label={label} className={classes.input} value={value} onChange={onChange} size={"small"} {...rest} disabled={lockControls}/>
}

const DataBlock = (props) => {

    const {name, target, lockControls} = props;

    const useStyles = makeStyles((theme) => ({
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
        },

    }));

    const classes = useStyles();

    return <Grid item sm={12} md={6}>
        <Paper>
            <div className={classes.formHeader}><Typography variant="h6">{name}</Typography></div>
            <form className={classes.form} noValidate autoComplete="on">
                {/*
                // Below trick is needed to avoid writing same target and lockControls attribute in each child
                // - parent expands children with these equal parameters by cloning them.
                // If it affects performance - just do copy/paste...
                */}
                {props.children.map((child) => (
                    <div key={child.props.name}>
                        {React.cloneElement(child, {target: target, lockControls: lockControls})}
                    </div>
                ))}
            </form>
        </Paper>
    </Grid>

}

export default function OrderHeader(props) {

    const {orderData, lockControls} = props;

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
    }));

    const classes = useStyles();

    return <Paper className={classes.paper}>
        <Grid container spacing={2}>
            <DataBlock name={"Buyer company"} target={orderData.buyerCompany} lockControls={lockControls}>
                <DataInput required name={"registrationName"}/>
                <DataInput required name={"legalIdentifier"}/>
                <DataInput required name={"partyIdentifier"}/>
            </DataBlock>
            <DataBlock name={"Buyer contact"} target={orderData.buyerContact} lockControls={lockControls}>
                <DataInput name={"personName"}/>
                <DataInput name={"email"}/>
                <DataInput name={"telephone"}/>
            </DataBlock>
        </Grid>
    </Paper>
}