import {Component, createRef, forwardRef} from "react";
import Stave from "./Stave";
import {Grid, CircularProgress} from "@mui/material";
import Loading from "./Loading";
import StaveWrapper from "./utils/StaveWrapper";

export default class Score extends Component {
    constructor(props) {
        super(props);
        this.id = props.id
        this.track = props.track
        this.state = {
            error: null,
            isLoaded: false,
            measures: [],
            stringCount: 6,
            tuning: "",
            wide: false,
            note: 0,
            measureObjs: [],
            measuresLengths: [],
            lastMeasure: 0,
            track: Number(this.track),
            start: 0,
            onStartChange: () => {}
        };

        this.setNote = (id) => {
            //console.log("SETNOTE " + id)
            this.setState({
                note: id
            })
        }

        this.setMeasure = (id) => {
            //console.log("SETMEASURE " + id)
            this.setState({
                lastMeasure: id
            })
        }
    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track;
        fetch(path).then(res => res.json()).then(
            (result) => {
                let measuresLengths = []
                for (let i = 0; i < result.measures.length; i++) {
                    measuresLengths.push(result.measures[i].beatDTOS.length);
                }
                this.setState({
                    isLoaded: true,
                    measures: result.measures,
                    stringCount: result.stringCount,
                    tuning: result.tunings[this.track].split(" ").reverse(),

                    measureObjs: Array(result.measures.length).fill().map((_, i) =>
                        this.state.measureObjs[i] || createRef()),
                    measuresLengths: measuresLengths
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

        const wide = measures.filter((m) => m.beatDTOS.filter((b) => b.noteDTOS.length > 0).length >= 25).length != 0

        let cols = wide ? {} : {xs: 4, sm: 8, md: 40}
        var totalMeasures = 0;

        if (error) {
            return <p>Error: {error.meassage} </p>
        } else if (!isLoaded) {
            return <Loading/>
        } else {
            return (
                <>
                    <Grid container spacing={0} columns={cols}>
                        {Array.from(Array(measures.length)).map((_, index) => {
                            let tempo = index === 0 || measures[index - 1].tempo !== measures[index].tempo ? measures[index].tempo : false;
                            let timeSignature = index === 0 || measures[index - 1].timeSignature !== measures[index].timeSignature ? measures[index].timeSignature : false;

                            totalMeasures++;

                            return (
                                <Grid item xs={2} sm={4} md={10} key={index} onClick={() => {
                                    this.setState({start: index})
                                    this.state.onStartChange();
                                }}>
                                    <Stave
                                        ref={this.state.measureObjs[index]}
                                        measure={measures[index].beatDTOS}
                                        tempo={tempo}
                                        stringCount={stringCount}
                                        timeSignature={timeSignature}
                                        tuning={index === 0 ? tuning : false}
                                        staveId={index + 1}
                                        pmIndexes={measures[index].pmIndexes}
                                        lrIndexes={measures[index].lrIndexes}
                                        slidesAndTies={measures[index].slidesAndTies}
                                        wide={wide}
                                        repeatStart={measures[index].repeatStart}
                                        repeatEnd={measures[index].repeatEnd}
                                        start={this.state.start === index}
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