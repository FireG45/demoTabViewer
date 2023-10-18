import { Component } from "react";
import Stave from "./Stave";
import { Grid, CircularProgress } from "@mui/material";

export default class Score extends Component {
    constructor(props) {
        super(props);
        this.id = props.id
        this.track = props.track
        this.state = {
            error : null,
            isLoaded : false,
            measures: [],
            stringCount : 6,
        };
    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track;
        fetch(path).then(res => res.json()).then(
            (result) => {
                this.setState({
                    isLoaded : true,
                    measures: result.measures,
                    stringCount : result.stringCount
                });
            },
            (error) => {
                this.setState({
                    isLoaded: true,
                    error
                })
            }
        )
    }

    render() {
        const {error, isLoaded, measures, stringCount} = this.state;
        if (error) {
            return <p>Error: {error.meassage} </p>
        } else if (!isLoaded) {
            return <CircularProgress />
        } else {
            return (
                <>
                    <Grid container spacing={0} columns={{ xs: 4, sm: 8, md: 15 }}>
                    {Array.from(Array(measures.length)).map((_, index) => (
                            <Grid item xs={2} sm={4} md={4.588} key={index}>
                                <Stave measure={measures[index].beatDTOS} stringCount={stringCount}/>
                            </Grid>
                        ))}
                    </Grid>
                </>
            )
        }
    }
}