import { TabSlide, TabTie } from 'vexflow'

export default function drawHummerSlide(slidesAndTies, notes, context) {
    for (let i = 0; i < slidesAndTies.length; i++) {
        let splitted = slidesAndTies[i].split("|")
        let from = notes[parseInt(splitted[0])]
        let to = notes[parseInt(splitted[1])]
        let indices = []
        if (!from || !to || !from.getPositions || !to.getPositions) continue;
        for (let i = 0; i < from.getPositions().length; i++) indices.push(i)
        var tie = splitted[2] === 'S' ? new TabSlide({
          first_note: from,
          last_note: to,
          first_indices: indices,
          last_indices: indices,
        }, from.getPositions()[0].fret < to.getPositions()[0].fret ? TabSlide.SLIDE_UP : TabSlide.SLIDE_DOWN)
        :
        new TabTie({
          first_note: from,
          last_note: to,
          first_indices: indices,
          last_indices: indices,
        }, from.getPositions()[0].fret < to.getPositions()[0].fret ? TabSlide.SLIDE_UP : TabSlide.SLIDE_DOWN);
  
        tie.setContext(context);
        tie.draw();
      }
}