import {Alert} from "@material-ui/lab";
import React from "react";
import {Snackbar} from "@material-ui/core";

export default function SmallSnackbar(props) {

    const {message = "Copied", opened, hide} = props;

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        hide();
    };

    return (
        <Snackbar open={opened} autoHideDuration={500} onClose={handleClose} anchorOrigin={{vertical: 'top', horizontal: 'center'}}>
            <Alert onClose={handleClose} severity="success" variant={"filled"}>{message}</Alert>
        </Snackbar>
    )
}