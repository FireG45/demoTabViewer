import React, {Component} from 'react'
import VexFlow from 'vexflow'
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

        stave.setContext(context).draw()
        var noteCount = 0
        let notes = []
        let beamNotes = []

        for (let i = 0; i < this.props.measure.length; i++) {
            let beat = this.props.measure[i].noteDTOS
            let duration = this.props.measure[i].duration
            let effects = this.props.measure[i].effects
            let ghostNote = this.props.measure[i].ghostNote
            let pos = []

            for (let j = 0; j < beat.length; j++) {
                pos.push({str: beat[j].string, fret: beat[j].fret})
            }

            noteCount += beat.length;

            if (pos.length > 0) {
                let note = new TabNote({positions: pos, duration: duration})
                note.setGhost(ghostNote)

                console.log(this.state.note + " " + noteCount)
                if (this.state.note === noteCount) {
                    //note.setStyle({fillStyle: "red", shadowBlur: 10, lineWidth: 5})
                }

                let parsedEffects = parseEffects(effects)

                for (let i = 0; i < parsedEffects.length; i++) {
                    const element = parsedEffects[i]
                    note.addModifier(element)
                }

                notes.push(note)
                beamNotes.push(note)
            } else {
                notes.push(new StaveNote({keys: ["b/4"], duration: duration + "r"}))
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
                {/*<h1>{this.state.note}</h1>*/}
                <canvas ref={this.container} style={{backgroundColor: this.state.note > 0 ? "#AAD1FF" : "white"}}/>
            </>
        )
    }
}

export default Stave
