import { GridList, GridListTile, GridListTileBar, IconButton, makeStyles } from "@material-ui/core";
import PageViewIcon from "@material-ui/icons/Pageview";

const useStyles = makeStyles(theme => ({
    gridList: {
        flexWrap: 'nowrap',
        // Promote the list into his own layer on Chrome. This cost memory but helps keeping high FPS.
        transform: 'translateZ(0)',
    },
    title: {
        color: 'white',
    },
    titleBar: {
        background: 'linear-gradient(to top, rgba(11,120,208, 1) 0%, rgba(11,120,208, 0.1) 70%, rgba(255,255,255,0.1) 100%)',
    },
}));

export default function RenderPictureList(props) {
    const classes = useStyles();

    return (
        <GridList className={classes.gridList} cols={3} cellHeight="360">
            {props.specList.map((spec) => (
                <GridListTile key={spec.attachment.externalReference.uri}>
                    <img src={spec.attachment.externalReference.uri} alt="Product" />
                    <GridListTileBar
                        classes={{
                            root: classes.titleBar,
                            title: classes.title,
                        }}
                        actionIcon={
                            <a aria-label={`visit url`} href={spec.attachment.externalReference.uri} target="_blank" rel="noreferrer">
                                <IconButton>
                                    <PageViewIcon className={classes.title} />
                                </IconButton>
                            </a>
                        }
                    />
                </GridListTile>
            )
            )}
        </GridList>
    )
}