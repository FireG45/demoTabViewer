import React, {Component} from 'react'
import VexFlow, {Fraction} from 'vexflow'
import drawHummerSlide from './renderUtils/drawHummerSlide'
import drawBeams from './renderUtils/drawBeams'
import {Box, Button, ClickAwayListener, Grid, Paper, Stack, Tooltip} from "@mui/material";

import Menu from "@mui/material/Menu";
import QuantityInput from "./QuantityInput";
import Dialog from "@mui/material/Dialog";
import {styled} from "@mui/system";
import parseEditEffects from "./renderUtils/parseEditEffects";

const {Renderer, TabStave, TabNote, Formatter, StaveNote} = VexFlow.Flow

class EditStave extends Component {
    constructor(props) {
        super(props)
        this.container = React.createRef()
        this.rendererRef = React.createRef()
        this.length = this.props.measure.length

        this.noteCount = 0;

        this.notes = []

        this.tempoField = React.createRef();

        this.showTempo = this.props.showTempo;
        this.tempo = this.props.tempo;

        this.effectsMap = []

        this.selectedNote = -1
        this.selectedBeat = -1
        this.selectedString = -1;
        this.noteJ = -1;

        this.slidesAndTies = this.props.slidesAndTies
        this.measure = this.props.measure;

        this.state = {
            note: 0,
            notes: [],
            menuAnchor: null,
            menuOpen: false,
            noteDialogOpen: false,
        }

        this.changes = []

        this.staveChanges = {
            staveId: this.props.staveId - 1,
            oldTempo: this.props.tempo,
            newTempo: this.props.tempo,
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

        this.handleContextMenuOpen = (event) => {
            event.preventDefault();
            this.setState({
                menuAnchor: event.currentTarget,
                openMenu: true,
            });
        }

        this.handleContextMenuClose = (event) => {
            this.setState({
                menuAnchor: null,
                openMenu: false,
            });
        }

        this.getShift = () => {
            return this.props.staveId === 1 || this.props.timeSignature ? 39 : 22
        }

        this.drawNoteSelection = (note, char = '▢') => {
            console.log("DRAW SELECTION")
            let stave = note.stave;
            let shift = this.getShift();

            let shiftX = stave.getX()
            let shiftY = stave.getY()
            let context = stave.getContext();
            let initFont = context.getFont();
            context.setFont({
                size: 20,
            });
            context
                .fillText(char, note.getX() + shiftX + shift - 10.5, note.getYs()[this.noteJ] + shiftY + 6.5);
            context.setFont(initFont);
        }

        this.handleClick = (event) => {
            console.clear()

            const DISTANCE = 8;

            let bounds = event.target.getBoundingClientRect();
            let x = event.clientX - bounds.left;
            let y = event.clientY - bounds.top;
            let shift = this.getShift();

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
            let beatIndex = 0;

            for (let n in this.state.notes) {
                let el = this.state.notes[n];
                let nn = el.postFormat();
                for (let i = 0; i < nn.getYs().length; i++) {
                    console.log()
                    if (distance(xx, nn.getX(), yy, nn.getYs()[i]) < DISTANCE) {
                        note = el;
                        noteJ = i;
                        break;
                    }
                    noteIndex++;
                }
                beatIndex++;
                if (note) break;
            }

            if (this.selectedNote >= 0 && this.selectedNote === noteIndex) {
                this.setState({
                    noteDialogOpen: true
                })
            }

            if (note !== null) {
                this.componentDidMount();
                this.drawNoteSelection(note);
                this.selectedNote = noteIndex;
                this.selectedBeat = beatIndex;
                this.selectedString = note.positions[noteJ].str;
                this.noteJ = noteJ;
                this.selectedNoteObj = note;
            } else {
                this.componentDidMount();
                this.selectedNote = -1;
            }

            this.setState({
                openMenu: false
            })

            console.log("ONCLICK NOTE: ", note);
        }

        this.changeEffects = () => {
            let noteIndex = this.selectedNote;
            this.effectsMap[noteIndex].filter((el) => el.changed).forEach((el) => {
                el.action();
                el.changed = false;
            });
            this.componentDidMount();
        }

        document.addEventListener(
            "keypress",
            (event) => {
                if (this.selectedNote === -1 || this.state.openMenu || this.state.noteDialogOpen) return;
                const keyName = event.key;
                let note = this.selectedNoteObj;
                if (!isNaN(keyName)) {
                    let noteID = this.selectedNote;
                    let noteDTO = this.measure[this.selectedBeat - 1].noteDTOS[this.noteJ];
                    if (noteDTO.fret === 'x') {
                        let beat = this.measure[this.selectedBeat - 1];
                        if (noteDTO.fret === 'x') {
                            noteDTO.fret = noteDTO.rawFret;
                        } else {
                            noteDTO.fret = 'x';
                        }
                    }

                    noteDTO.fret = noteDTO.fret + keyName
                    if (noteDTO.fret.length > 2 || noteDTO.fret[0] === '0' || Number(noteDTO.fret) > 24)
                        noteDTO.fret = keyName;
                    this.componentDidMount();
                    this.selectedNote = noteID;
                    this.drawNoteSelection(note);
                    this.changes[this.selectedNote].oldFret = this.changes[this.selectedNote].newFret;
                    this.changes[this.selectedNote].newFret = noteDTO.fret;
                }
            },
            false,
        );
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

        const changeBooleanEffect = (effect, beat) => {
            let ind = beat.effects.indexOf(effect);
            if (ind === -1) {
                beat.effects.push(effect);
                let removedInd = this.changes[this.selectedNote].removedEffects.indexOf(effect);
                if (removedInd !== -1)
                    delete this.changes[this.selectedNote].removedEffects[removedInd];
                else
                    this.changes[this.selectedNote].addedEffects.push(effect);
            } else {
                delete beat.effects[ind];
                let addedInd = this.changes[this.selectedNote].addedEffects.indexOf(effect);
                if (addedInd !== -1)
                    delete this.changes[this.selectedNote].addedEffects[addedInd];
                else
                    this.changes[this.selectedNote].removedEffects.push(effect);
            }
        }

        const changeBooleanHammerSlideEffect = (effect, beat) => {
            let i = this.selectedBeat - 1;
            let search = `${i}|${i + 1}|${effect}|${this.noteJ}`;
            let ind = this.slidesAndTies.indexOf(search);
            if (ind === -1) {
                this.slidesAndTies.push(search);
            } else {
                delete this.slidesAndTies[ind];
            }
        }

        let allNotesCount = 0;
        for (let i = 0; i < this.measure.length; i++) {
            let beat = this.measure[i];
            for (let j = 0; j < beat.noteDTOS.length; j++) {
                allNotesCount += beat.noteDTOS.length
            }
        }


        for (let i = 0; i < this.measure.length; i++) {
            let beat = this.measure[i];
            let noteId = 0;
            for (let j = 0; j < beat.noteDTOS.length; j++) {
                let noteDTO = beat.noteDTOS[j];
                let map = [
                    {
                        name: 'P.M', selected: false, title: "Palm Mute", action: () => {
                            changeBooleanEffect('p', beat)
                        }
                    },
                    {
                        name: 'T', selected: false, title: "Тэппинг", action: () => {
                            changeBooleanEffect('t', beat)
                        }
                    },
                    {
                        name: 'S', selected: false, title: "Слэп", action: () => {
                            changeBooleanEffect('s', beat)
                        }
                    },
                    {
                        name: 'P', selected: false, title: "Поппинг", action: () => {
                            changeBooleanEffect('P', beat)
                        }
                    },
                    {
                        name: 'A.H', selected: false, title: "Флажолет", action: () => {
                            changeBooleanEffect('h|H|' + noteDTO.fret, beat);
                        }
                    },
                    {
                        name: '>', selected: false, title: "Акцентированная нота", action: () => {
                            changeBooleanEffect('>', beat);
                        }
                    },
                    {
                        name: 'L.R', selected: false, title: "Let Ring", action: () => {
                            changeBooleanEffect('L', beat);
                        }
                    },
                    {
                        name: '^',
                        selected: false,
                        changed: false,
                        title: "Сильно акцентированная нота",
                        action: () => {
                            changeBooleanEffect('^', beat);
                        }
                    },

                    {
                        name: '~', selected: false, changed: false, title: "Вибрато", action: () => {
                            changeBooleanEffect('v', beat)
                        }
                    },
                    {
                        name: 'X', selected: false, changed: false, title: "Мертвая нота", action: () => {
                            if (noteDTO.fret === 'x') {
                                noteDTO.fret = noteDTO.rawFret;
                            } else {
                                noteDTO.fret = 'x';
                            }
                            this.changes[this.selectedNote].oldFret = this.changes[this.selectedNote].newFret;
                            this.changes[this.selectedNote].newFret = noteDTO.fret;
                        }
                    },
                    {
                        name: '()', selected: false, changed: false, title: "Нота призрак", action: () => {
                            if (!beat.ghostNote) {
                                let removedInd = this.changes[this.selectedNote].removedEffects.indexOf('g');
                                if (removedInd !== -1)
                                    delete this.changes[this.selectedNote].removedEffects[removedInd];
                                else
                                    this.changes[this.selectedNote].addedEffects.push('g');
                            } else {
                                let addedInd = this.changes[this.selectedNote].addedEffects.indexOf('g');
                                if (addedInd !== -1)
                                    delete this.changes[this.selectedNote].addedEffects[addedInd];
                                else
                                    this.changes[this.selectedNote].removedEffects.push('g');
                            }
                            beat.ghostNote = !beat.ghostNote;
                        }
                    },
                    {
                        name: 'H.O', selected: false, changed: false, title: "Hammer-on", action: () => {
                            changeBooleanEffect('H', beat)
                            changeBooleanHammerSlideEffect('H', beat);
                        }
                    },
                    {
                        name: '⎯', selected: false, changed: false, title: "Слайд", action: () => {
                            changeBooleanEffect('S', beat)
                            changeBooleanHammerSlideEffect('S', beat);
                        }
                    },
                ]

                let note = beat.noteDTOS[j];
                if (note.effectsFlags.length > 0) {
                    for (let l = 0; l < note.effectsFlags.length; l++) {
                        map[l].selected = note.effectsFlags[l];
                    }
                }

                // if (this.effectsMap.length < beat.noteDTOS.length)
                this.effectsMap.push(map)

                if (this.changes.length < allNotesCount)
                    this.changes.push({
                        noteId: ++noteId,
                        beatId: i,
                        oldFret: noteDTO.fret,
                        newFret: noteDTO.fret,
                        addedEffects: [],
                        removedEffects: [],
                    })
            }
        }

        if (this.showTempo) {
            const bpm = this.tempo
            const x = this.measure[0] && this.measure[0].effects[0] === 't' ? -12 : 0
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

        for (let i = 0; i < this.measure.length; i++) {
            let beat = this.measure[i].noteDTOS
            let duration = this.measure[i].duration
            let effects = this.measure[i].effects
            let ghostNote = this.measure[i].ghostNote
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

                let parsedEffects = parseEditEffects(effects)

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

        //drawPalmMutes(this.props.pmIndexes, this.notes, context, shift)
        //drawLetRing(this.props.lrIndexes, this.notes, context, shift)

        drawBeams(this.notes, context)
        drawHummerSlide(this.slidesAndTies, this.notes, context)

        ex.setState({
            notes: ex.notes
        })
    }

    render() {
        let bgColor = "white";
        if (this.state.note > 0) bgColor = "#AAD1FF"
        else if (this.props.start) bgColor = "#e6e6e6";

        const Item = styled(Paper)(({theme}) => ({
            backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
            padding: theme.spacing(2),
            textAlign: 'center',
        }));

        return (
            <>
                <ClickAwayListener onClickAway={this.selectedNote > 0 ? this.handleClickAway : () => {
                }}>
                    <canvas onClick={this.handleClick} ref={this.container} onContextMenu={this.handleContextMenuOpen}/>
                </ClickAwayListener>

                <Menu
                    open={this.state.openMenu}
                    anchorEl={this.state.menuAnchor}
                >
                    <ClickAwayListener onClickAway={(event) => {
                        this.setState({
                            openMenu: false
                        })
                    }}>
                        <Stack direction={'column'} spacing={1} sx={{mx: 'auto', p: '10px'}}>
                            <h5>Изменить темп</h5>
                            <QuantityInput startVal={this.tempo} ref={this.tempoField}/>
                            <Button fullWidth variant={'contained'} type={'info'} onClick={(event) => {
                                this.tempo = this.tempoField.current.state.value;
                                this.staveChanges.oldTempo = this.staveChanges.newTempo;
                                this.staveChanges.newTempo = this.tempo;
                                this.setState({
                                    openMenu: false
                                });
                                this.componentDidMount();
                            }}>
                                Сохранить
                            </Button>
                        </Stack>
                    </ClickAwayListener>
                </Menu>

                <Dialog open={this.state.noteDialogOpen}>
                    <Stack sx={{mx: 'auto', p: '10px'}} spacing={2}>
                        <h5>Изменить эффекты</h5>
                        <ClickAwayListener onClickAway={(event) => {
                            this.setState({
                                noteDialogOpen: false
                            })
                        }}>
                            <Box sx={{flexGrow: 1}}>
                                <Grid container spacing={{xs: 0.5, md: 0.5}} columns={{xs: 2, sm: 8, md: 14}}>
                                    {this.selectedNote >= 0 ?
                                        this.effectsMap[this.selectedNote].map((el, index) => (
                                            <Grid item xs={1} sm={2} md={2} key={index}>
                                                <Tooltip title={el.title}>
                                                    <Button fullWidth variant={el.selected ? 'contained' : 'outlined'}
                                                            onClick={() => {
                                                                el.selected = !el.selected;
                                                                // el.changed = !el.changed;
                                                                el.action();
                                                                this.componentDidMount();
                                                                this.setState({
                                                                    noteDialogOpen: false
                                                                })
                                                            }}>
                                                        {el.name}
                                                    </Button>
                                                </Tooltip>
                                            </Grid>
                                        )) :
                                        <></>
                                    }
                                </Grid>
                            </Box>
                        </ClickAwayListener>
                    </Stack>
                </Dialog>
            </>
        )
    }
}

export default EditStave
