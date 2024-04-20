import React from 'react';
import { SnackbarProvider, useSnackbar } from 'notistack';

export default function SnackbarHandler({ onShowSnackbar }) {
    const { enqueueSnackbar } = useSnackbar();

    // Function to show a snackbar with a message
    const showSnackbar = (message) => {
        enqueueSnackbar(message);
        if (onShowSnackbar) {
            onShowSnackbar(message); // Call external callback if provided
        }
    };

    return (
        <SnackbarProvider>
            {/* No buttons or other UI elements here */}
        </SnackbarProvider>
    );
}
