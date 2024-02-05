import { Component } from "react";
import Stave from "./Stave";
import { Grid, CircularProgress } from "@mui/material";
import Loading from "./Loading";

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
            tuning : "",
        };
    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track;
        fetch(path).then(res => res.json()).then(
            (result) => {
                this.setState({
                    isLoaded : true,
                    measures: result.measures,
                    stringCount : result.stringCount,
                    tuning : result.tunings[this.track].split(" ").reverse(),
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
        const {error, isLoaded, measures, stringCount, tuning} = this.state;
        if (error) {
            return <p>Error: {error.meassage} </p>
        } else if (!isLoaded) {
            return <Loading />
        } else {
            return (
                <>
                    <Grid container spacing={0} columns={{ xs: 4, sm: 8, md: 40 }}>
                    {Array.from(Array(measures.length)).map((_, index) => {
                        let tempo = index === 0 || measures[index - 1].tempo !== measures[index].tempo ? measures[index].tempo : false;
                        let timeSignature = index === 0 || measures[index - 1].timeSignature !== measures[index].timeSignature ? measures[index].timeSignature : false;
                        return (
                            <Grid item xs={2} sm={4} md={10} key={index}>
                                <Stave
                                    measure={measures[index].beatDTOS}
                                    tempo={tempo}
                                    stringCount={stringCount}
                                    timeSignature={timeSignature}
                                    tuning={index === 0 ? tuning : false}
                                    staveId={index + 1}
                                    pmIndexes={measures[index].pmIndexes}
                                    lrIndexes={measures[index].lrIndexes}
                                    slidesAndTies={measures[index].slidesAndTies}
                                />
                            </Grid>
                        )
                    })}
                    </Grid>
                </>
            )
        }
    }
}