import CatalogIcon from '@material-ui/icons/Mail';
import { Badge, makeStyles } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
    badge: {
        marginRight: '15px',
    },
}));


export default function CatalogBadge(props) {
    const { index, code } = props;
    const classes = useStyles();

    return (
        <Badge key={code} badgeContent={(index + 1)} color="primary" className={classes.badge} title={code}>
            <CatalogIcon color="secondary" fontSize="small"/>
        </Badge>
    )
} 