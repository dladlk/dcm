import { Badge, makeStyles } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
    badge: {
        paddingLeft: '10px',
        marginRight: '15px',
    },
}));


export default function CatalogBadge(props) {
    const { index, code } = props;
    const classes = useStyles();

    return (
        <Badge key={code} badgeContent={(index + 1)} color="primary" className={classes.badge} title={code}/>
    )
} 