import {forwardRef} from "react";
import Stave from "../Stave";

const StaveWrapper = forwardRef(function StaveWrapper(props, ref) {
    return (
        <Stave {...props} ref={ref} />
    )
});

export default StaveWrapper;