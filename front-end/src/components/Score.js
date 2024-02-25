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
            error: null,
            isLoaded: false,
            measures: [],
            stringCount: 6,
            tuning: "",
            wide: false,
            note : 0,
        };

        this.setNote = (id) => {
            console.log("SETNOTE " + id)
            this.setState({
                note : id
            })
        }
    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track;
        fetch(path).then(res => res.json()).then(
            (result) => {
                this.setState({
                    isLoaded: true,
                    measures: result.measures,
                    stringCount: result.stringCount,
                    tuning: result.tunings[this.track].split(" ").reverse(),
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
        const { error, isLoaded, measures, stringCount, tuning } = this.state;

        const wide = measures.filter((m) => m.beatDTOS.filter((b) => b.noteDTOS.length > 0).length >= 25).length != 0

        let cols = wide ? {} : { xs: 4, sm: 8, md: 40 }

        var totalNotes = 0;
        var totalNotes_1 = 0;

        var currNote = this.state.note + 1;

        if (error) {
            return <p>Error: {error.meassage} </p>
        } else if (!isLoaded) {
            return <Loading />
        } else {
            return (
                <>
                    <Grid container spacing={0} columns={cols}>
                        {Array.from(Array(measures.length)).map((_, index) => {
                            let tempo = index === 0 || measures[index - 1].tempo !== measures[index].tempo ? measures[index].tempo : false;
                            let timeSignature = index === 0 || measures[index - 1].timeSignature !== measures[index].timeSignature ? measures[index].timeSignature : false;
                            let currNoteCount = 0
                            for (let i = 0; i < measures[index].beatDTOS.length; i++) {
                                totalNotes += measures[index].beatDTOS[i].noteDTOS.length;
                                currNoteCount += measures[index].beatDTOS[i].noteDTOS.length;
                            }
                            if (index > 0 ) {
                                for (let i = 0; i < measures[index - 1].beatDTOS.length; i++) {
                                    totalNotes_1 += measures[index - 1].beatDTOS[i].noteDTOS.length;
                                }
                            } else {
                                totalNotes_1 = 1;
                            }
                            let highlightNote = totalNotes_1 - 1 <= currNote && currNote <= totalNotes + 1
                                    ? currNote % (currNoteCount + 1) : null
                            let shift = index === 0 ? 0 : 1;
                            return (
                                <Grid item xs={2} sm={4} md={10} key={index}>
                                    <h1>HN: {highlightNote} tn1: {totalNotes_1 - 1} tn: {totalNotes + 1} </h1>
                                    <h1>NOTE: {currNote} count: {currNoteCount + 1}</h1>
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
                                        wide={wide}
                                        highlightNote={totalNotes_1 <= currNote && currNote <= totalNotes
                                            ? (currNote + shift) % (currNoteCount + 1) : null}
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