import { Card, CardContent, Fab, makeStyles, Typography } from "@material-ui/core";
import RefreshIcon from '@material-ui/icons/Refresh';

const useStyles = makeStyles(theme => ({
    cardContent: {
        '&:last-child': {
            paddingBottom: theme.spacing(2),
        },
    },
    row: {
        display: "flex",
    },
    header: {
        flex: '1',
    },
    buttons: {
        flex: '1',
        display: "flex",
        placeContent: 'stretch flex-end',
        alignItems: 'stretch',

        '& button': {
            marginLeft: theme.spacing(4),
        }
    }
}));

export default function ProductListHeader(prop) {
    const { name, refreshAction } = prop;

    const classes = useStyles();

    return (
        <Card style={{ marginTop: '16px', marginBottom: '16px' }}>
            <CardContent className={classes.cardContent}>
                <div className={classes.row} >
                    <div className={classes.header}>
                        <Typography variant="h4">{name}</Typography>
                    </div>

                    <div className={classes.buttons}>
                        <Fab color="primary" aria-label="Refresh" size="small">
                            <RefreshIcon onClick = {() => refreshAction()}/>
                        </Fab>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
} 