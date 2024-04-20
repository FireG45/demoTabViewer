import React, {Component, createRef, forwardRef} from "react";
import Stave from "./Stave";
import {Grid, CircularProgress, Snackbar} from "@mui/material";
import Loading from "./Loading";
import StaveWrapper from "./utils/StaveWrapper";
import EditStave from "./EditStave";
import SnackbarHandler from "./SnackbarHandler";
import {useSnackbar} from "notistack";

const UNDO_TIMEOUT = 500;

export default class EditScore extends Component {
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
            snackbarRef: createRef(),
            measuresLengths: [],
            lastMeasure: 0,
            track: Number(this.track),
            start: 0,
            lastState: [],
            undoCount: 0,
            lastUndo: Date.now(),

            setLastState: (lastState) => {
                this.setState({
                    lastState: [...this.state.lastState, lastState]
                });
                console.clear();
                console.log(this.state.lastState);
            },

            popLastState: () => {
                if (this.state.snackbarRef.current) this.state.snackbarRef.current.showSnackbar('UNDO!');
                let lastStates = this.state.lastState;
                if (lastStates.length === 0) return;
                let lastState = this.state.lastState.pop();
                this.state.measureObjs[lastState.staveId - 1].current.popState(lastState);
                this.state.measureObjs[lastState.staveId - 1].current.componentDidMount();
                console.log('POP');
            },

            onStartChange: () => {
            }
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

        this.getStavesChanges = () => {
            let v = this.state.measureObjs;
            let changes = []
            v.forEach((el) => {
                if (el.current.staveChanges.oldTempo !== el.current.staveChanges.newTempo) {
                    changes.push(el.current.staveChanges)
                }
            });
            return changes;
        }

        this.getChanges = () => {
            let v = this.state.measureObjs;
            let changes = []
            v.forEach((el) => {
                for (const elElement of el.current.changes) {
                    if (elElement.oldFret !== elElement.newFret || elElement.addedEffects.length !== 0 ||
                        elElement.removedEffects.length !== 0) {
                        elElement.staveId = el.current.props.staveId - 1;
                        changes.push(elElement)
                    }
                }
            });
            return changes;
        }

        this.undoCount = 0;

    }

    componentDidMount() {
        var path = "http://localhost:8080/tabs/" + this.id + "?track=" + this.track;

        const timerId = setInterval(() => {
            this.setState({ canUndo: true });
            clearInterval(timerId);
        }, 200);


        let ex = this;
        if (this.undoCount === 0) {
            document.addEventListener('keydown', function (event) {
                if (Date.now() - ex.state.lastUndo > UNDO_TIMEOUT && event.ctrlKey && event.key === 'z') {
                    ex.state.popLastState();
                    ex.setState({
                        lastUndo: Date.now()
                    })
                }
            });
        }


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
                            let tempo = measures[index].tempo;
                            let showTempo = index === 0 || measures[index - 1].tempo !== measures[index].tempo
                            let timeSignature = index === 0 || measures[index - 1].timeSignature !== measures[index].timeSignature ? measures[index].timeSignature : false;

                            totalMeasures++;

                            return (
                                <Grid item xs={2} sm={4} md={10} key={index} onClick={() => {
                                    //this.setState({start: index})
                                    //this.state.onStartChange();
                                }}>
                                    <SnackbarHandler ref={this.state.snackbarRef}/>
                                    <EditStave
                                        setLastState={this.state.setLastState}
                                        ref={this.state.measureObjs[index]}
                                        measure={measures[index].beatDTOS}
                                        tempo={tempo}
                                        showTempo={true}
                                        stringCount={stringCount}
                                        timeSignature={timeSignature}
                                        tuning={false}//index === 0 ? tuning : false}
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