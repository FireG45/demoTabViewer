import { Beam } from 'vexflow'


export default function drawBeams(notes, context) {
    var beams = Beam.generateBeams(notes, {
        stem_direction: -1, flat_beams :true, flat_beam_offset : -10
      });
  
      beams.forEach(function(beam) {
        beam.setContext(context).draw();
      });
}