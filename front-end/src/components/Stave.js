import React, {Component} from 'react'
import VexFlow, {Fraction, Modifier} from 'vexflow'
import parseEffects from './renderUtils/parseEffects'
import drawHummerSlide from './renderUtils/drawHummerSlide'
import drawBeams from './renderUtils/drawBeams'
import drawPalmMutes from './renderUtils/drawPalmMutes'
import drawLetRing from './renderUtils/drawLetRing'
import {ClickAwayListener} from "@mui/material";

const {Renderer, TabStave, TabNote, Formatter, StaveNote} = VexFlow.Flow

class Stave extends Component {
    constructor(props) {
        super(props)
        this.container = React.createRef()
        this.rendererRef = React.createRef()
        this.length = this.props.measure.length

        this.noteCount = 0;

        this.notes = []

        this.state = {
            note: 0,
            selectedNote: -1,
            notes: []
        }

        this.setNote = (noteID) => {
            //console.log("SETNOTE: " + noteID);
            this.setState({
                note: noteID
            })
        }

        this.handleClickAway = () => {
            this.componentDidMount();
        }

        this.handleClick = (event) => {
            console.clear()

            const DISTANCE = 10;

            let bounds = event.target.getBoundingClientRect();
            let x = event.clientX - bounds.left;
            let y = event.clientY - bounds.top;
            let shift = this.props.tuning ? 40 : 20
            const distance =
                (x1, x2, y1, y2) => Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            let stave = this.state.notes[0].stave;

            let shiftX = stave.getX()
            let shiftY = stave.getY();

            let xx = x - shiftX - shift;
            let yy = y - shiftY;
            let note = null;
            let noteJ = 0;
            let noteIndex = 0;
            for (let n in this.state.notes) {
                let el = this.state.notes[n];
                let nn = el.postFormat();
                for (let i = 0; i < nn.getYs().length; i++) {
                    if (distance(xx, nn.getX(), yy, nn.getYs()[i]) < DISTANCE) {
                        note = el;
                        noteJ = i;
                        break;
                    }
                    noteIndex++;
                }
                if (note) break;
            }
            if (note) {
                this.componentDidMount();
                let context = stave.getContext();
                let initFont = context.getFont();
                context.setFont({
                    size: 17,
                });
                context
                    .fillText('▢', note.getX() + shiftX + shift - 9.5, note.getYs()[noteJ] + shiftY + 4.5);
                context.setFont(initFont);
                this.setState({
                    selectedNote: 1
                })
            } else {
                this.componentDidMount();
                this.setState({
                    selectedNote: -1
                })
            }
            if (note !== null && note.positions !== undefined)
                console.log("Clicked on: " + note.positions[noteJ].fret + " : " + note.positions[noteJ].str)
        }
    }


    componentDidMount() {
        if (this.rendererRef.current == null) {
            this.rendererRef.current = new Renderer(
                this.container.current,
                Renderer.Backends.CANVAS
            )
        }
        const renderer = this.rendererRef.current

        const ex = this;

        renderer.resize(500 * (this.props.wide ? 4 : 1), 160 + (this.props.stringCount % 6 * 15))
        const context = renderer.getContext()

        let shift = this.props.tuning ? 20 : 0
        const stave = new TabStave(shift, 0, 448 * (this.props.wide ? 4 : 1) - shift)
        stave.options.num_lines = this.props.stringCount
        stave.options.line_config = new Array(this.props.stringCount).fill({visible: true})

        if (this.props.tempo) {
            const bpm = this.props.tempo
            const x = this.props.measure[0] && this.props.measure[0].effects[0] === 't' ? -12 : 0
            const y = 0
            context.setFont("Arial", 20)
            context.fillText("♩", y + 5, x + 25)
            context.setFont("Arial", 12)
            context.fillText(bpm, y, x + 40)
        }

        if (this.props.staveId) {
            const id = this.props.staveId
            context.setFont("Arial", 8)
            context.fillText(id, shift, 50)
        }

        if (this.props.tuning && this.props.tuning.length > 0) {
            context.setFont("Arial", 10)
            for (let i = 1; i < this.props.tuning.length; i++) {
                const element = this.props.tuning[i - 1]
                context.fillText(element, 0, i * 13 + 43)
            }
        }

        if (this.props.timeSignature) {
            stave.addTimeSignature(this.props.timeSignature)
        }

        // if (this.props.repeatStart) stave.setBegBarType(VexFlow.Barline.type.REPEAT_BEGIN);
        // if (this.props.repeatEnd > 0) {
        //     stave.setEndBarType(VexFlow.Barline.type.REPEAT_END);
        //     context.setFont("Arial", 10);
        //     context.fillText("x" + this.props.repeatEnd, 430, 50);
        // }

        stave.setContext(context).draw()
        this.noteCount = 0
        this.notes = []
        let beamNotes = []

        function readDuration(duration) {
            let dDotted = 0;
            dDotted += duration[0] === '.' ? 1 : 0;
            dDotted += duration[1] === '.' ? 1 : 0;
            duration = duration.slice(dDotted);
            let fraction;
            switch (duration) {
                case "w" :
                    fraction = new Fraction(1, 1);
                    break;
                case "h" :
                    fraction = new Fraction(1, 2);
                    break;
                case "q" :
                    fraction = new Fraction(1, 4);
                    break;
                default :
                    fraction = new Fraction(1, parseInt(duration));
                    break;
            }

            for (let i = 0; i < dDotted; i++)
                fraction.add(fraction.multiply(new Fraction(1, 2)));

            return fraction;
        }

        for (let i = 0; i < this.props.measure.length; i++) {
            let beat = this.props.measure[i].noteDTOS
            let duration = this.props.measure[i].duration
            let effects = this.props.measure[i].effects
            let ghostNote = this.props.measure[i].ghostNote
            let pos = []

            for (let j = 0; j < beat.length; j++) {
                pos.push({str: beat[j].string, fret: beat[j].fret})
            }

            this.noteCount++;

            let note;
            if (pos.length > 0) {
                note = new TabNote({positions: pos, duration: duration})
                note.setGhost(ghostNote)
                note.setDuration(readDuration(duration))

                let parsedEffects = parseEffects(effects)

                for (let i = 0; i < parsedEffects.length; i++) {
                    const element = parsedEffects[i]
                    note.addModifier(element)
                }

                this.notes.push(note)
                beamNotes.push(note)
            } else {
                note = new StaveNote({keys: ["b/4"], duration: duration + "r"});
                this.notes.push(note)
            }
            note.setDuration(readDuration(duration))
            if (this.state.note === this.noteCount) {
                note.setStyle({fillStyle: "red", shadowBlur: 10, lineWidth: 5, strokeStyle: "red"})
            }
        }


        if (this.notes && this.notes.length > 0) {
            stave.setStyle({strokeStyle: "red"})
            Formatter.FormatAndDraw(context, stave, this.notes)
        }

        drawPalmMutes(this.props.pmIndexes, this.notes, context, shift)
        drawLetRing(this.props.lrIndexes, this.notes, context, shift)

        drawBeams(this.notes, context)
        drawHummerSlide(this.props.slidesAndTies, this.notes, context)

        ex.setState({
            notes: ex.notes
        })
    }

    render() {
        let bgColor = "white";
        if (this.state.note > 0) bgColor = "#AAD1FF"
        else if (this.props.start) bgColor = "#e6e6e6";
        return (
            <>
                <canvas ref={this.container} style={{backgroundColor: bgColor}}/>
            </>
        )
    }
}

export default Stave
