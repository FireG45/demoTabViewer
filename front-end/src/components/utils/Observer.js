import {useEffect} from "react";

export default function Observer ({ value, didUpdate }) {
    useEffect(() => {
        didUpdate(value)
    }, [value])
    return null
}