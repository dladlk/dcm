import { Card, CardContent, Fab, makeStyles, Typography } from "@material-ui/core";
import ArrowIcon from '@material-ui/icons/KeyboardBackspaceOutlined';
import RefreshIcon from '@material-ui/icons/Refresh';
import { useHistory } from "react-router";

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

export default function DetailHeader(prop) {
    const { name } = prop;

    const classes = useStyles();

    const history = useHistory();

    const handleBack = () => {
        console.log('Go back');
        history.goBack();
    }

    return (
        <Card style={{ marginTop: '16px' }}>
            <CardContent className={classes.cardContent}>
                <div className={classes.row} >
                    <div className={classes.header}>
                        <Typography variant="h4">{name}</Typography>
                    </div>
                    <div className={classes.buttons}>
                        <Fab color="primary" aria-label="Previous" size="small">
                            <ArrowIcon style={{ transform: 'rotate(90deg)' }} />
                        </Fab>
                        <Fab color="primary" aria-label="Next" size="small">
                            <ArrowIcon style={{ transform: 'rotate(270deg)' }} />
                        </Fab>
                        <Fab color="primary" aria-label="Refresh" size="small">
                            <RefreshIcon />
                        </Fab>
                        <Fab color="primary" aria-label="Back" size="small" onClick={ handleBack }>
                            <ArrowIcon />
                        </Fab>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
} 