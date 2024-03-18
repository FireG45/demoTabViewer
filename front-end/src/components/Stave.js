import React, {Component} from 'react'
import VexFlow, {Fraction, Modifier} from 'vexflow'
import parseEffects from './renderUtils/parseEffects'
import drawHummerSlide from './renderUtils/drawHummerSlide'
import drawBeams from './renderUtils/drawBeams'
import drawPalmMutes from './renderUtils/drawPalmMutes'
import drawLetRing from './renderUtils/drawLetRing'

const {Renderer, TabStave, TabNote, Formatter, StaveNote} = VexFlow.Flow

class Stave extends Component {
    constructor(props) {
        super(props)
        this.container = React.createRef()
        this.rendererRef = React.createRef()
        this.length = this.props.measure.length

        this.noteCount = 0;

        this.state = {
            note: 0,
        }

        this.setNote = (noteID) => {
            //console.log("SETNOTE: " + noteID);
            this.setState({
                note: noteID
            })
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
        let notes = []
        let beamNotes = []

        function readDuration(duration) {
            let dDotted = 0;
            dDotted += duration[0] === '.' ? 1 : 0;
            dDotted += duration[1] === '.' ? 1 : 0;
            duration = duration.slice(dDotted);
            let fraction;
            switch (duration) {
                case "w" : fraction = new Fraction(1, 1); break;
                case "h" : fraction = new Fraction(1, 2); break;
                case "q" : fraction = new Fraction(1, 4); break;
                default : fraction = new Fraction(1, parseInt(duration)); break;
            }

            for (let i= 0; i < dDotted; i++)
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

                notes.push(note)
                beamNotes.push(note)
            } else {
                note = new StaveNote({keys: ["b/4"], duration: duration + "r"});
                notes.push(note)
            }
            note.setDuration(readDuration(duration))
            if (this.state.note === this.noteCount) {
                note.setStyle({fillStyle: "red", shadowBlur: 10, lineWidth: 5, strokeStyle: "red"})
            }
        }


        if (notes && notes.length > 0) {
            stave.setStyle({strokeStyle: "red"})
            Formatter.FormatAndDraw(context, stave, notes)
        }

        drawPalmMutes(this.props.pmIndexes, notes, context, shift)
        drawLetRing(this.props.lrIndexes, notes, context, shift)

        drawBeams(notes, context)
        drawHummerSlide(this.props.slidesAndTies, notes, context)
    }

    render() {
        // if (this.state.note > 0) this.container.current.scrollIntoView({block: "start", behavior: "auto"});
        return (
            <>
                {/*<h1>{this.state.note} {this.noteCount}</h1>*/}
                <canvas ref={this.container} style={{backgroundColor: this.state.note > 0 ? "#AAD1FF" : "white"}}/>
                {/*<canvas ref={this.container}/>*/}
            </>
        )
    }
}

export default Stave
