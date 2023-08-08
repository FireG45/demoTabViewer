package org.herac.tuxguitar.graphics.control.painters;

import org.herac.tuxguitar.graphics.command.TGCubicTo;
import org.herac.tuxguitar.graphics.command.TGLineTo;
import org.herac.tuxguitar.graphics.command.TGMoveTo;
import org.herac.tuxguitar.graphics.command.TGPaintCommand;
import org.herac.tuxguitar.graphics.command.TGPaintModel;
import org.herac.tuxguitar.ui.resource.UIPainter;

public class TGClefPainter {
	
	private static final TGPaintCommand TREBLE_MODEL = new TGPaintModel(
		new TGMoveTo(0.9706216f, -0.9855771f),
		new TGCubicTo(0.99023926f, -0.99538594f, 0.99350905f, -0.99538594f, 1.0131269f, -0.99538594f),
		new TGCubicTo(1.0392835f, -0.99211615f, 1.055632f, -0.9823073f, 1.0915977f, -0.9430719f),
		new TGCubicTo(1.3270102f, -0.7011198f, 1.5231876f, -0.26953024f, 1.572232f, 0.09666765f),
		new TGCubicTo(1.578771f, 0.1555208f, 1.578771f, 0.29284477f, 1.572232f, 0.35496712f),
		new TGCubicTo(1.5362663f, 0.6917379f, 1.3956721f, 0.9990827f, 1.0719799f, 1.4404812f),
		new TGLineTo(1.0262054f, 1.502604f),
		new TGLineTo(1.0523622f, 1.577805f),
		new TGCubicTo(1.1144851f, 1.7576342f, 1.1864164f, 1.9766989f, 1.2321913f, 2.1369102f),
		new TGCubicTo(1.24527f, 2.1761456f, 1.2550789f, 2.208842f, 1.2550789f, 2.212112f),
		new TGCubicTo(1.2550789f, 2.212112f, 1.2779659f, 2.212112f, 1.3008534f, 2.2153816f),
		new TGCubicTo(1.4152898f, 2.22519f, 1.5133789f, 2.2513473f, 1.6147372f, 2.3003914f),
		new TGCubicTo(1.6964773f, 2.3428962f, 1.7684091f, 2.3919404f, 1.8370711f, 2.457333f),
		new TGCubicTo(1.9122725f, 2.5325344f, 1.9613168f, 2.601196f, 2.0070913f, 2.6960156f),
		new TGCubicTo(2.0757532f, 2.8333395f, 2.10518f, 2.9772024f, 2.10191f, 3.121066f),
		new TGCubicTo(2.0986407f, 3.2126155f, 2.085562f, 3.2812777f, 2.0561357f, 3.3662882f),
		new TGCubicTo(2.0169f, 3.4905329f, 1.9449685f, 3.60497f, 1.8468798f, 3.7030587f),
		new TGCubicTo(1.7618695f, 3.7880688f, 1.6833986f, 3.8403826f, 1.578771f, 3.8828878f),
		new TGLineTo(1.5395356f, 3.8992357f),
		new TGLineTo(1.5395356f, 4.016942f),
		new TGCubicTo(1.5395356f, 4.183693f, 1.5297267f, 4.37987f, 1.516648f, 4.497576f),
		new TGCubicTo(1.50357f, 4.6185517f, 1.4708736f, 4.732989f, 1.4185596f, 4.837617f),
		new TGCubicTo(1.2812357f, 5.1155343f, 1.0392835f, 5.262667f, 0.7679054f, 5.2365108f),
		new TGCubicTo(0.46056065f, 5.2070837f, 0.21207006f, 4.997828f, 0.14994715f, 4.716641f),
		new TGCubicTo(0.12052006f, 4.589125f, 0.13359922f, 4.491037f, 0.18591277f, 4.409296f),
		new TGCubicTo(0.25457475f, 4.3013988f, 0.38535964f, 4.2392764f, 0.5128747f, 4.2425456f),
		new TGCubicTo(0.6632773f, 4.2490854f, 0.78425336f, 4.3667912f, 0.80714035f, 4.5302725f),
		new TGCubicTo(0.8234888f, 4.661057f, 0.7679054f, 4.782033f, 0.6632773f, 4.8474255f),
		new TGCubicTo(0.6044242f, 4.8833914f, 0.52268356f, 4.8997393f, 0.46056065f, 4.8899307f),
		new TGCubicTo(0.45075235f, 4.8899307f, 0.44094297f, 4.8866606f, 0.44094297f, 4.8899307f),
		new TGLineTo(0.48017892f, 4.929166f),
		new TGCubicTo(0.55211014f, 5.0010977f, 0.63385075f, 5.0468726f, 0.7384789f, 5.06976f),
		new TGCubicTo(0.76136583f, 5.0730295f, 0.78098357f, 5.0730295f, 0.8332976f, 5.0730295f),
		new TGCubicTo(0.89215076f, 5.0730295f, 0.8986898f, 5.0730295f, 0.9313861f, 5.0664897f),
		new TGCubicTo(0.97716117f, 5.0534115f, 1.0163965f, 5.0370636f, 1.0523622f, 5.0174456f),
		new TGCubicTo(1.2158434f, 4.919357f, 1.3270102f, 4.716641f, 1.3564366f, 4.47142f),
		new TGCubicTo(1.3662455f, 4.37987f, 1.3760543f, 4.183693f, 1.3760543f, 4.0398297f),
		new TGCubicTo(1.3760543f, 3.9450107f, 1.3760543f, 3.9384713f, 1.3695153f, 3.9384713f),
		new TGCubicTo(1.3564366f, 3.9450107f, 1.2877747f, 3.95155f, 1.2387304f, 3.9548192f),
		new TGCubicTo(1.1766075f, 3.9580889f, 1.0719799f, 3.9580889f, 1.0229356f, 3.95155f),
		new TGCubicTo(0.8300278f, 3.9286623f, 0.65346843f, 3.8632698f, 0.4997966f, 3.755372f),
		new TGCubicTo(0.2709232f, 3.595161f, 0.10744194f, 3.3564782f, 0.038779963f, 3.0818305f),
		new TGCubicTo(-0.04949972f, 2.7385209f, 0.012623194f, 2.3723233f, 0.22841798f, 1.9865077f),
		new TGCubicTo(0.31996745f, 1.8262968f, 0.40824714f, 1.6955118f, 0.6273117f, 1.4045155f),
		new TGLineTo(0.751557f, 1.2377651f),
		new TGLineTo(0.72866946f, 1.1756423f),
		new TGCubicTo(0.5717278f, 0.7604004f, 0.51941377f, 0.52171814f, 0.5030659f, 0.17513847f),
		new TGCubicTo(0.49652684f, 0.024735928f, 0.5030659f, -0.102779746f, 0.52268356f, -0.2074073f),
		new TGCubicTo(0.5717278f, -0.48205525f, 0.69924295f, -0.72727656f, 0.8986898f, -0.92345417f),
		new TGCubicTo(0.9379252f, -0.9626896f, 0.9542737f, -0.9757682f, 0.9706216f, -0.9855771f),
		new TGMoveTo(1.2289215f, -0.4264719f),
		new TGCubicTo(1.1962258f, -0.48205525f, 1.2027647f, -0.478786f, 1.1733383f, -0.45916772f),
		new TGCubicTo(1.0817888f, -0.40358436f, 0.98697f, -0.3185745f, 0.9183075f, -0.23356462f),
		new TGCubicTo(0.77771425f, -0.05373496f, 0.69924295f, 0.1555208f, 0.68943465f, 0.38112438f),
		new TGCubicTo(0.6861648f, 0.4726739f, 0.69597363f, 0.5315269f, 0.7384789f, 0.6721202f),
		new TGCubicTo(0.77117467f, 0.7865572f, 0.8627241f, 1.0644748f, 0.86926377f, 1.0710139f),
		new TGCubicTo(0.86926377f, 1.0742836f, 0.88888097f, 1.0513968f, 0.90849864f, 1.0219696f),
		new TGCubicTo(1.1341028f, 0.6982775f, 1.2550789f, 0.43343806f, 1.3008534f, 0.16859889f),
		new TGCubicTo(1.3106622f, 0.09993696f, 1.3172013f, 0.044353604f, 1.320471f, -0.03411722f),
		new TGCubicTo(1.3237408f, -0.2139464f, 1.3041232f, -0.30222607f, 1.2289215f, -0.4264719f),
		new TGMoveTo(0.9477346f, 1.7739828f),
		new TGCubicTo(0.9281169f, 1.71186f, 0.90849864f, 1.6628156f, 0.90849864f, 1.6628156f),
		new TGCubicTo(0.9052294f, 1.6628156f, 0.7679054f, 1.8459139f, 0.7090518f, 1.9276547f),
		new TGCubicTo(0.5161445f, 2.1990333f, 0.39843827f, 2.4180977f, 0.32977578f, 2.6338923f),
		new TGCubicTo(0.2840013f, 2.7679467f, 0.26438358f, 2.8921926f, 0.25784454f, 3.0295167f),
		new TGCubicTo(0.25784454f, 3.1047182f, 0.26111433f, 3.1504927f, 0.27419245f, 3.2060761f),
		new TGCubicTo(0.34939402f, 3.4970722f, 0.6600081f, 3.7357545f, 1.0262054f, 3.7880688f),
		new TGCubicTo(1.0817888f, 3.7946076f, 1.2387304f, 3.7946076f, 1.3073924f, 3.7880688f),
		new TGCubicTo(1.3760543f, 3.7782598f, 1.3695153f, 3.7815294f, 1.3662455f, 3.7521029f),
		new TGCubicTo(1.3466283f, 3.4414887f, 1.320471f, 3.2518506f, 1.2681575f, 2.9706633f),
		new TGCubicTo(1.2485392f, 2.8529572f, 1.192956f, 2.604466f, 1.1896861f, 2.601196f),
		new TGCubicTo(1.1864164f, 2.5979269f, 1.1242939f, 2.604466f, 1.0883284f, 2.614275f),
		new TGCubicTo(1.0425533f, 2.6240838f, 1.0131269f, 2.6338923f, 0.9738914f, 2.6535103f),
		new TGCubicTo(0.76136583f, 2.7614079f, 0.69924295f, 3.0066295f, 0.8332976f, 3.2060761f),
		new TGCubicTo(0.85618514f, 3.2387724f, 0.9150382f, 3.2976255f, 0.9510039f, 3.3205128f),
		new TGCubicTo(0.96408254f, 3.3303218f, 0.99350905f, 3.3499393f, 1.0131269f, 3.3597484f),
		new TGCubicTo(1.0425533f, 3.3760962f, 1.0490924f, 3.3826356f, 1.055632f, 3.3957143f),
		new TGCubicTo(1.0817888f, 3.4382195f, 1.0654408f, 3.4905329f, 1.0163965f, 3.5134206f),
		new TGCubicTo(0.99023926f, 3.5264988f, 0.9706216f, 3.5232296f, 0.9183075f, 3.4970722f),
		new TGCubicTo(0.79079294f, 3.4349499f, 0.69597363f, 3.3499393f, 0.6273117f, 3.2453117f),
		new TGCubicTo(0.4997966f, 3.0491343f, 0.48344818f, 2.7973735f, 0.5880763f, 2.5848482f),
		new TGCubicTo(0.67962575f, 2.4017498f, 0.84310645f, 2.2775042f, 1.0523622f, 2.2317295f),
		new TGCubicTo(1.0719799f, 2.22519f, 1.0883284f, 2.2219207f, 1.0883284f, 2.2219207f),
		new TGCubicTo(1.0915977f, 2.2186508f, 1.0000482f, 1.9211154f, 0.9477346f, 1.7739828f),
		new TGMoveTo(1.3924028f, 2.617545f),
		new TGCubicTo(1.382594f, 2.617545f, 1.3727851f, 2.614275f, 1.3695153f, 2.614275f),
		new TGLineTo(1.3597065f, 2.611005f),
		new TGLineTo(1.3760543f, 2.6796675f),
		new TGCubicTo(1.4512559f, 3.0098987f, 1.5068393f, 3.369557f, 1.526457f, 3.6736321f),
		new TGCubicTo(1.5297267f, 3.7063284f, 1.5297267f, 3.7324853f, 1.5297267f, 3.7324853f),
		new TGCubicTo(1.5329965f, 3.7324853f, 1.5755012f, 3.7128677f, 1.5983888f, 3.6965194f),
		new TGCubicTo(1.6539721f, 3.6638227f, 1.7062862f, 3.6147785f, 1.7487912f, 3.559195f),
		new TGCubicTo(1.8501498f, 3.4251404f, 1.8861152f, 3.25839f, 1.8468798f, 3.0883694f),
		new TGCubicTo(1.8174533f, 2.9510455f, 1.7389826f, 2.8235304f, 1.6278152f, 2.735251f),
		new TGCubicTo(1.5591533f, 2.6829367f, 1.4676039f, 2.6371622f, 1.3924028f, 2.617545f)
	);
	
	private static final TGPaintCommand BASS_MODEL = new TGPaintModel(
		new TGMoveTo(0.71937084f, 0.16147426f),
		new TGCubicTo(0.75454587f, 0.15827677f, 0.8920496f, 0.16147426f, 0.94321334f, 0.16467176f),
		new TGCubicTo(1.3429334f, 0.20944051f, 1.6147422f, 0.4077018f, 1.7042797f, 0.72108173f),
		new TGCubicTo(1.7266634f, 0.8074205f, 1.7362571f, 0.87137556f, 1.7330583f, 0.9800993f),
		new TGCubicTo(1.7330583f, 1.1176031f, 1.7170696f, 1.2327217f, 1.6723021f, 1.3542367f),
		new TGCubicTo(1.5092158f, 1.8370967f, 1.0327508f, 2.236817f, 0.16296211f, 2.6205468f),
		new TGCubicTo(0.12778586f, 2.6365356f, 0.095808364f, 2.6525242f, 0.092610866f, 2.655722f),
		new TGCubicTo(0.073424615f, 2.668513f, 0.054238364f, 2.6717105f, 0.035052113f, 2.6621182f),
		new TGCubicTo(0.019063365f, 2.655722f, 0.012667115f, 2.6493268f, 0.006272115f, 2.633338f),
		new TGCubicTo(-0.0033216353f, 2.6173494f, -1.2413526E-4f, 2.6013606f, 0.006272115f, 2.5885694f),
		new TGCubicTo(0.012667115f, 2.575778f, 0.025458366f, 2.5661855f, 0.095808364f, 2.5246143f),
		new TGCubicTo(0.3804096f, 2.355133f, 0.58826333f, 2.2048392f, 0.7641396f, 2.0513468f),
		new TGCubicTo(0.8185009f, 2.000183f, 0.9208296f, 1.8978542f, 0.96559834f, 1.8466904f),
		new TGCubicTo(1.1606609f, 1.6196505f, 1.2629896f, 1.3990055f, 1.2981646f, 1.1431843f),
		new TGCubicTo(1.3045596f, 1.0824268f, 1.3045596f, 0.93852806f, 1.2981646f, 0.87777054f),
		new TGCubicTo(1.2853733f, 0.7850368f, 1.2629896f, 0.69869673f, 1.2342097f, 0.6219505f),
		new TGCubicTo(1.2118247f, 0.5707868f, 1.2054296f, 0.554798f, 1.1798471f, 0.51322675f),
		new TGCubicTo(1.0871121f, 0.35653678f, 0.94641083f, 0.27019802f, 0.7897221f, 0.27019802f),
		new TGCubicTo(0.63622963f, 0.27019802f, 0.5083196f, 0.35653678f, 0.42517713f, 0.51962304f),
		new TGCubicTo(0.4123871f, 0.5452043f, 0.3804096f, 0.6283468f, 0.3804096f, 0.63154423f),
		new TGCubicTo(0.3804096f, 0.6347418f, 0.39319962f, 0.63154423f, 0.40599087f, 0.63154423f),
		new TGCubicTo(0.4795396f, 0.6283468f, 0.55948335f, 0.65712553f, 0.62024087f, 0.705093f),
		new TGCubicTo(0.7353596f, 0.80422306f, 0.7673371f, 0.9641105f, 0.6969871f, 1.1080092f),
		new TGCubicTo(0.64902085f, 1.2135355f, 0.54669213f, 1.280688f, 0.43477085f, 1.2902818f),
		new TGCubicTo(0.27808085f, 1.2998742f, 0.12458836f, 1.1975467f, 0.073424615f, 1.0504493f),
		new TGCubicTo(0.057435866f, 1.002483f, 0.054238364f, 0.95451677f, 0.060633365f, 0.87777054f),
		new TGCubicTo(0.08941337f, 0.55799556f, 0.29406962f, 0.28618675f, 0.5754721f, 0.19345176f),
		new TGCubicTo(0.62343836f, 0.17746301f, 0.6714046f, 0.16786925f, 0.71937084f, 0.16147426f),
		new TGMoveTo(1.9632971f, 0.462063f),
		new TGCubicTo(2.0144608f, 0.44607428f, 2.0752184f, 0.462063f, 2.1167896f, 0.5004368f),
		new TGCubicTo(2.1455696f, 0.53241426f, 2.1615584f, 0.57398427f, 2.1615584f, 0.6155555f),
		new TGCubicTo(2.1615584f, 0.7018943f, 2.091207f, 0.7722455f, 2.0048683f, 0.7722455f),
		new TGCubicTo(1.9153309f, 0.7722455f, 1.8449808f, 0.7018943f, 1.8449808f, 0.6155555f),
		new TGCubicTo(1.8449808f, 0.54200673f, 1.8929471f, 0.48124927f, 1.9632971f, 0.462063f),
		new TGMoveTo(1.9632971f, 1.2583042f),
		new TGCubicTo(2.0144608f, 1.2423155f, 2.0752184f, 1.2583042f, 2.1167896f, 1.2966768f),
		new TGCubicTo(2.155162f, 1.338248f, 2.171151f, 1.3958068f, 2.155162f, 1.4469718f),
		new TGCubicTo(2.1391733f, 1.5205193f, 2.0784159f, 1.5684855f, 2.0048683f, 1.5684855f),
		new TGCubicTo(1.9153309f, 1.5684855f, 1.8449808f, 1.4981354f, 1.8449808f, 1.4117955f),
		new TGCubicTo(1.8449808f, 1.338248f, 1.8929471f, 1.2774905f, 1.9632971f, 1.2583042f)
	);
	
	private static final TGPaintCommand ALTO_MODEL = new TGPaintModel(
		new TGMoveTo(0.026545623f, 0.5470838f),
		new TGCubicTo(0.03553187f, 0.5410925f, 0.053504374f, 0.5410925f, 0.21525937f, 0.5410925f),
		new TGLineTo(0.39498562f, 0.5410925f),
		new TGLineTo(0.40397188f, 0.55007875f),
		new TGCubicTo(0.40996313f, 0.55607f, 0.41595438f, 0.56206f, 0.41894937f, 0.5680513f),
		new TGCubicTo(0.42494062f, 0.5800325f, 0.42494062f, 0.69086504f, 0.42494062f, 2.038815f),
		new TGCubicTo(0.42494062f, 3.386765f, 0.42494062f, 3.4975975f, 0.41894937f, 3.5095787f),
		new TGCubicTo(0.41595438f, 3.51557f, 0.40996313f, 3.52156f, 0.40397188f, 3.5275512f),
		new TGLineTo(0.39498562f, 3.5335424f),
		new TGLineTo(0.21226312f, 3.5335424f),
		new TGLineTo(0.029541872f, 3.5335424f),
		new TGLineTo(0.020554373f, 3.5275512f),
		new TGCubicTo(0.014564373f, 3.52156f, 0.008573122f, 3.51557f, 0.0055781226f, 3.5095787f),
		new TGCubicTo(-4.131275E-4f, 3.4975975f, -4.131275E-4f, 3.386765f, -4.131275E-4f, 2.03582f),
		new TGLineTo(0.0025818725f, 0.5710463f),
		new TGLineTo(0.008573122f, 0.56206f),
		new TGCubicTo(0.014564373f, 0.55607f, 0.020554373f, 0.55007875f, 0.026545623f, 0.5470838f),
		new TGMoveTo(0.6016719f, 0.5470838f),
		new TGCubicTo(0.6106581f, 0.5410925f, 0.6196444f, 0.5410925f, 0.6525944f, 0.5410925f),
		new TGCubicTo(0.6915344f, 0.5410925f, 0.6945306f, 0.5440875f, 0.7035169f, 0.55007875f),
		new TGCubicTo(0.72748065f, 0.5680513f, 0.7244844f, 0.49915627f, 0.7244844f, 1.2779713f),
		new TGLineTo(0.7244844f, 1.9759114f),
		new TGLineTo(0.74245685f, 1.9609337f),
		new TGCubicTo(0.8173431f, 1.8890426f, 0.8862381f, 1.7782114f, 0.92517936f, 1.6793613f),
		new TGCubicTo(0.9491431f, 1.6074712f, 0.9641206f, 1.5265937f, 0.9701106f, 1.4397264f),
		new TGCubicTo(0.9701106f, 1.400785f, 0.97310686f, 1.3947937f, 0.97909814f, 1.3858075f),
		new TGCubicTo(0.9850881f, 1.3768213f, 1.0060569f, 1.36484f, 1.0210332f, 1.36484f),
		new TGCubicTo(1.0360106f, 1.36484f, 1.0539831f, 1.3738263f, 1.0629693f, 1.3828125f),
		new TGCubicTo(1.0689607f, 1.3947937f, 1.0719569f, 1.39779f, 1.0749519f, 1.4547038f),
		new TGCubicTo(1.0839381f, 1.5775163f, 1.1258744f, 1.6793613f, 1.1947694f, 1.7512524f),
		new TGCubicTo(1.2636644f, 1.8171525f, 1.3445419f, 1.8441112f, 1.4433919f, 1.8321288f),
		new TGCubicTo(1.5122869f, 1.8231425f, 1.5632094f, 1.802175f, 1.6021507f, 1.7632337f),
		new TGCubicTo(1.6440868f, 1.7212975f, 1.6680493f, 1.6733713f, 1.6860231f, 1.598485f),
		new TGCubicTo(1.7039956f, 1.532585f, 1.7099856f, 1.466685f, 1.7129818f, 1.3318901f),
		new TGCubicTo(1.7189732f, 0.86759627f, 1.6321044f, 0.67888254f, 1.3894731f, 0.6459325f),
		new TGCubicTo(1.2936194f, 0.63395125f, 1.2307143f, 0.6519238f, 1.2037556f, 0.696855f),
		new TGCubicTo(1.1977656f, 0.7088375f, 1.1977656f, 0.7118325f, 1.1977656f, 0.7357963f),
		new TGCubicTo(1.1977656f, 0.76275504f, 1.1977656f, 0.76575124f, 1.2067518f, 0.7837238f),
		new TGCubicTo(1.2157382f, 0.8016963f, 1.2217281f, 0.81068254f, 1.2576743f, 0.84662753f),
		new TGCubicTo(1.2936194f, 0.8795775f, 1.2996106f, 0.89156f, 1.3085968f, 0.9065363f),
		new TGCubicTo(1.3295643f, 0.95146877f, 1.3205781f, 1.0173688f, 1.2876282f, 1.0772775f),
		new TGCubicTo(1.2756469f, 1.098245f, 1.2337106f, 1.1401813f, 1.2127419f, 1.1521637f),
		new TGCubicTo(1.1378556f, 1.197095f, 1.0509881f, 1.197095f, 0.9761019f, 1.1521637f),
		new TGCubicTo(0.9551344f, 1.1401813f, 0.9131981f, 1.098245f, 0.9012156f, 1.0772775f),
		new TGCubicTo(0.8712619f, 1.026355f, 0.8622756f, 0.9724363f, 0.8712619f, 0.90354127f),
		new TGCubicTo(0.8862381f, 0.81667376f, 0.92517936f, 0.74178755f, 0.9880844f, 0.6758875f),
		new TGCubicTo(1.0539831f, 0.6129825f, 1.1348606f, 0.5710463f, 1.2307143f, 0.55007875f),
		new TGCubicTo(1.2876282f, 0.53809625f, 1.3984594f, 0.53809625f, 1.4943144f, 0.55307376f),
		new TGCubicTo(1.8477769f, 0.6039963f, 2.0364895f, 0.81966877f, 2.0634482f, 1.197095f),
		new TGCubicTo(2.0724356f, 1.3139175f, 2.0634482f, 1.39779f, 2.0364895f, 1.4846575f),
		new TGCubicTo(1.9526169f, 1.7302837f, 1.7279594f, 1.9070151f, 1.4583694f, 1.933975f),
		new TGCubicTo(1.3715006f, 1.9429612f, 1.3475369f, 1.93697f, 1.2666606f, 1.8830512f),
		new TGCubicTo(1.2187331f, 1.8501024f, 1.1947694f, 1.83812f, 1.1708056f, 1.8321288f),
		new TGCubicTo(1.1348606f, 1.8231425f, 1.0989156f, 1.8321288f, 1.0719569f, 1.8560925f),
		new TGCubicTo(1.0390068f, 1.8830512f, 1.0240294f, 1.939965f, 1.0240294f, 2.038815f),
		new TGCubicTo(1.0240294f, 2.137665f, 1.0390068f, 2.194579f, 1.0719569f, 2.2215376f),
		new TGCubicTo(1.0989156f, 2.2455013f, 1.1348606f, 2.2544875f, 1.1708056f, 2.2455013f),
		new TGCubicTo(1.1947694f, 2.23951f, 1.2187331f, 2.2275276f, 1.2666606f, 2.194579f),
		new TGCubicTo(1.3475369f, 2.14066f, 1.3715006f, 2.1346688f, 1.4583694f, 2.143655f),
		new TGCubicTo(1.7279594f, 2.170615f, 1.9526169f, 2.3473463f, 2.0364895f, 2.5929725f),
		new TGCubicTo(2.0634482f, 2.6798398f, 2.0724356f, 2.7637124f, 2.0634482f, 2.8775399f),
		new TGCubicTo(2.0334945f, 3.2909112f, 1.8178219f, 3.5035875f, 1.4044507f, 3.5335424f),
		new TGCubicTo(1.3056006f, 3.5395336f, 1.2367057f, 3.5335424f, 1.1678107f, 3.5095787f),
		new TGCubicTo(1.0330156f, 3.4646475f, 0.92218435f, 3.3568113f, 0.88324314f, 3.2280061f),
		new TGCubicTo(0.8592794f, 3.1381438f, 0.8652706f, 3.0662525f, 0.9012156f, 3.0003524f),
		new TGCubicTo(0.9131981f, 2.979385f, 0.9551344f, 2.9374487f, 0.9761019f, 2.9254663f),
		new TGCubicTo(1.0509881f, 2.880535f, 1.1378556f, 2.880535f, 1.2127419f, 2.9254663f),
		new TGCubicTo(1.2337106f, 2.9374487f, 1.2756469f, 2.979385f, 1.2876282f, 3.0003524f),
		new TGCubicTo(1.3205781f, 3.0602612f, 1.3295643f, 3.126161f, 1.3085968f, 3.1710937f),
		new TGCubicTo(1.2996106f, 3.18607f, 1.2936194f, 3.1980524f, 1.2576743f, 3.2310026f),
		new TGCubicTo(1.2217281f, 3.2669475f, 1.2157382f, 3.2759337f, 1.2067518f, 3.2939062f),
		new TGCubicTo(1.1977656f, 3.3118787f, 1.1977656f, 3.314875f, 1.1977656f, 3.3388388f),
		new TGCubicTo(1.1977656f, 3.3657975f, 1.1977656f, 3.3687925f, 1.2037556f, 3.380775f),
		new TGCubicTo(1.2307143f, 3.4257061f, 1.2936194f, 3.4436786f, 1.3894731f, 3.4316974f),
		new TGCubicTo(1.5362506f, 3.4107287f, 1.6261132f, 3.3328474f, 1.6710457f, 3.1770837f),
		new TGCubicTo(1.7009994f, 3.0752387f, 1.7159768f, 2.9374487f, 1.7129818f, 2.74574f),
		new TGCubicTo(1.7099856f, 2.610945f, 1.7039956f, 2.545045f, 1.6860231f, 2.47615f),
		new TGCubicTo(1.6500769f, 2.3323689f, 1.5751907f, 2.2604775f, 1.4433919f, 2.2455013f),
		new TGCubicTo(1.3445419f, 2.2335188f, 1.2636644f, 2.2604775f, 1.1947694f, 2.3263776f),
		new TGCubicTo(1.1258744f, 2.3982687f, 1.0839381f, 2.5001137f, 1.0749519f, 2.6229262f),
		new TGCubicTo(1.0719569f, 2.6798398f, 1.0689607f, 2.6828363f, 1.0629693f, 2.6948175f),
		new TGCubicTo(1.0539831f, 2.7038038f, 1.0360106f, 2.71279f, 1.0210332f, 2.71279f),
		new TGCubicTo(1.0060569f, 2.71279f, 0.9850881f, 2.7008088f, 0.97909814f, 2.6918225f),
		new TGCubicTo(0.97310686f, 2.6828363f, 0.9701106f, 2.676845f, 0.9701106f, 2.6379037f),
		new TGCubicTo(0.9641206f, 2.5180862f, 0.9401569f, 2.4282224f, 0.8922294f, 2.3233826f),
		new TGCubicTo(0.8562844f, 2.2514913f, 0.79637563f, 2.170615f, 0.74245685f, 2.1166964f),
		new TGLineTo(0.7244844f, 2.1017187f),
		new TGLineTo(0.7244844f, 2.7996588f),
		new TGCubicTo(0.7244844f, 3.5784738f, 0.72748065f, 3.5095787f, 0.7035169f, 3.5275512f),
		new TGCubicTo(0.6945306f, 3.5335424f, 0.6915344f, 3.5335424f, 0.6495981f, 3.5335424f),
		new TGCubicTo(0.6076619f, 3.5335424f, 0.6046669f, 3.5335424f, 0.59568065f, 3.5275512f),
		new TGCubicTo(0.5896894f, 3.52156f, 0.58369815f, 3.51557f, 0.58070314f, 3.5095787f),
		new TGCubicTo(0.57471186f, 3.4975975f, 0.57471186f, 3.386765f, 0.57471186f, 2.03582f),
		new TGLineTo(0.5777081f, 0.5710463f),
		new TGLineTo(0.58369815f, 0.56206f),
		new TGCubicTo(0.5896894f, 0.55607f, 0.59568065f, 0.55007875f, 0.6016719f, 0.5470838f)
	);
	
	private static final TGPaintCommand TENOR_MODEL = new TGPaintModel(
		new TGMoveTo(0.026545623f, -0.45291623f),
		new TGCubicTo(0.03553187f, -0.45890749f, 0.053504374f, -0.45890749f, 0.21525937f, -0.45890749f),
		new TGLineTo(0.39498562f, -0.45890749f),
		new TGLineTo(0.40397188f, -0.44992122f),
		new TGCubicTo(0.40996313f, -0.44392997f, 0.41595438f, -0.43793997f, 0.41894937f, -0.43194872f),
		new TGCubicTo(0.42494062f, -0.41996747f, 0.42494062f, -0.30913496f, 0.42494062f, 1.038815f),
		new TGCubicTo(0.42494062f, 2.386765f, 0.42494062f, 2.4975975f, 0.41894937f, 2.5095787f),
		new TGCubicTo(0.41595438f, 2.51557f, 0.40996313f, 2.52156f, 0.40397188f, 2.5275512f),
		new TGLineTo(0.39498562f, 2.5335424f),
		new TGLineTo(0.21226312f, 2.5335424f),
		new TGLineTo(0.029541872f, 2.5335424f),
		new TGLineTo(0.020554373f, 2.5275512f),
		new TGCubicTo(0.014564373f, 2.52156f, 0.008573122f, 2.51557f, 0.0055781226f, 2.5095787f),
		new TGCubicTo(-4.131275E-4f, 2.4975975f, -4.131275E-4f, 2.386765f, -4.131275E-4f, 1.03582f),
		new TGLineTo(0.0025818725f, -0.42895374f),
		new TGLineTo(0.008573122f, -0.43793997f),
		new TGCubicTo(0.014564373f, -0.44392997f, 0.020554373f, -0.44992122f, 0.026545623f, -0.45291623f),
		new TGMoveTo(0.6016719f, -0.45291623f),
		new TGCubicTo(0.6106581f, -0.45890749f, 0.6196444f, -0.45890749f, 0.6525944f, -0.45890749f),
		new TGCubicTo(0.6915344f, -0.45890749f, 0.6945306f, -0.45591247f, 0.7035169f, -0.44992122f),
		new TGCubicTo(0.72748065f, -0.43194872f, 0.7244844f, -0.5008437f, 0.7244844f, 0.27797127f),
		new TGLineTo(0.7244844f, 0.9759113f),
		new TGLineTo(0.74245685f, 0.96093374f),
		new TGCubicTo(0.8173431f, 0.88904256f, 0.8862381f, 0.7782113f, 0.92517936f, 0.6793613f),
		new TGCubicTo(0.9491431f, 0.6074713f, 0.9641206f, 0.52659374f, 0.9701106f, 0.4397263f),
		new TGCubicTo(0.9701106f, 0.40078503f, 0.97310686f, 0.39479375f, 0.97909814f, 0.3858075f),
		new TGCubicTo(0.9850881f, 0.37682128f, 1.0060569f, 0.36484003f, 1.0210332f, 0.36484003f),
		new TGCubicTo(1.0360106f, 0.36484003f, 1.0539831f, 0.37382627f, 1.0629693f, 0.3828125f),
		new TGCubicTo(1.0689607f, 0.39479375f, 1.0719569f, 0.39779f, 1.0749519f, 0.45470375f),
		new TGCubicTo(1.0839381f, 0.57751626f, 1.1258744f, 0.6793613f, 1.1947694f, 0.7512525f),
		new TGCubicTo(1.2636644f, 0.81715256f, 1.3445419f, 0.84411126f, 1.4433919f, 0.8321288f),
		new TGCubicTo(1.5122869f, 0.82314247f, 1.5632094f, 0.802175f, 1.6021507f, 0.7632337f),
		new TGCubicTo(1.6440868f, 0.72129756f, 1.6680493f, 0.67337126f, 1.6860231f, 0.59848505f),
		new TGCubicTo(1.7039956f, 0.532585f, 1.7099856f, 0.466685f, 1.7129818f, 0.33189005f),
		new TGCubicTo(1.7189732f, -0.13240373f, 1.6321044f, -0.32111746f, 1.3894731f, -0.35406747f),
		new TGCubicTo(1.2936194f, -0.36604872f, 1.2307143f, -0.34807622f, 1.2037556f, -0.303145f),
		new TGCubicTo(1.1977656f, -0.2911625f, 1.1977656f, -0.28816748f, 1.1977656f, -0.26420373f),
		new TGCubicTo(1.1977656f, -0.23724498f, 1.1977656f, -0.23424873f, 1.2067518f, -0.21627623f),
		new TGCubicTo(1.2157382f, -0.19830373f, 1.2217281f, -0.18931746f, 1.2576743f, -0.15337247f),
		new TGCubicTo(1.2936194f, -0.12042248f, 1.2996106f, -0.10843998f, 1.3085968f, -0.09346372f),
		new TGCubicTo(1.3295643f, -0.048531234f, 1.3205781f, 0.017368764f, 1.2876282f, 0.07727754f),
		new TGCubicTo(1.2756469f, 0.098245025f, 1.2337106f, 0.14018124f, 1.2127419f, 0.15216374f),
		new TGCubicTo(1.1378556f, 0.19709504f, 1.0509881f, 0.19709504f, 0.9761019f, 0.15216374f),
		new TGCubicTo(0.9551344f, 0.14018124f, 0.9131981f, 0.098245025f, 0.9012156f, 0.07727754f),
		new TGCubicTo(0.8712619f, 0.026355028f, 0.8622756f, -0.027563721f, 0.8712619f, -0.09645873f),
		new TGCubicTo(0.8862381f, -0.18332621f, 0.92517936f, -0.25821248f, 0.9880844f, -0.32411247f),
		new TGCubicTo(1.0539831f, -0.3870175f, 1.1348606f, -0.42895374f, 1.2307143f, -0.44992122f),
		new TGCubicTo(1.2876282f, -0.46190372f, 1.3984594f, -0.46190372f, 1.4943144f, -0.44692624f),
		new TGCubicTo(1.8477769f, -0.39600372f, 2.0364895f, -0.18033123f, 2.0634482f, 0.19709504f),
		new TGCubicTo(2.0724356f, 0.31391752f, 2.0634482f, 0.39779f, 2.0364895f, 0.48465753f),
		new TGCubicTo(1.9526169f, 0.7302838f, 1.7279594f, 0.907015f, 1.4583694f, 0.93397504f),
		new TGCubicTo(1.3715006f, 0.9429613f, 1.3475369f, 0.93697006f, 1.2666606f, 0.8830512f),
		new TGCubicTo(1.2187331f, 0.8501025f, 1.1947694f, 0.83812004f, 1.1708056f, 0.8321288f),
		new TGCubicTo(1.1348606f, 0.82314247f, 1.0989156f, 0.8321288f, 1.0719569f, 0.8560925f),
		new TGCubicTo(1.0390068f, 0.8830512f, 1.0240294f, 0.93996507f, 1.0240294f, 1.038815f),
		new TGCubicTo(1.0240294f, 1.137665f, 1.0390068f, 1.1945789f, 1.0719569f, 1.2215376f),
		new TGCubicTo(1.0989156f, 1.2455013f, 1.1348606f, 1.2544875f, 1.1708056f, 1.2455013f),
		new TGCubicTo(1.1947694f, 1.23951f, 1.2187331f, 1.2275276f, 1.2666606f, 1.1945789f),
		new TGCubicTo(1.3475369f, 1.14066f, 1.3715006f, 1.1346688f, 1.4583694f, 1.1436551f),
		new TGCubicTo(1.7279594f, 1.170615f, 1.9526169f, 1.3473463f, 2.0364895f, 1.5929725f),
		new TGCubicTo(2.0634482f, 1.6798398f, 2.0724356f, 1.7637124f, 2.0634482f, 1.8775399f),
		new TGCubicTo(2.0334945f, 2.2909112f, 1.8178219f, 2.5035875f, 1.4044507f, 2.5335424f),
		new TGCubicTo(1.3056006f, 2.5395336f, 1.2367057f, 2.5335424f, 1.1678107f, 2.5095787f),
		new TGCubicTo(1.0330156f, 2.4646475f, 0.92218435f, 2.3568113f, 0.88324314f, 2.2280061f),
		new TGCubicTo(0.8592794f, 2.1381438f, 0.8652706f, 2.0662525f, 0.9012156f, 2.0003524f),
		new TGCubicTo(0.9131981f, 1.9793849f, 0.9551344f, 1.9374487f, 0.9761019f, 1.9254663f),
		new TGCubicTo(1.0509881f, 1.8805349f, 1.1378556f, 1.8805349f, 1.2127419f, 1.9254663f),
		new TGCubicTo(1.2337106f, 1.9374487f, 1.2756469f, 1.9793849f, 1.2876282f, 2.0003524f),
		new TGCubicTo(1.3205781f, 2.0602612f, 1.3295643f, 2.126161f, 1.3085968f, 2.1710937f),
		new TGCubicTo(1.2996106f, 2.18607f, 1.2936194f, 2.1980524f, 1.2576743f, 2.2310026f),
		new TGCubicTo(1.2217281f, 2.2669475f, 1.2157382f, 2.2759337f, 1.2067518f, 2.2939062f),
		new TGCubicTo(1.1977656f, 2.3118787f, 1.1977656f, 2.314875f, 1.1977656f, 2.3388388f),
		new TGCubicTo(1.1977656f, 2.3657975f, 1.1977656f, 2.3687925f, 1.2037556f, 2.380775f),
		new TGCubicTo(1.2307143f, 2.4257061f, 1.2936194f, 2.4436786f, 1.3894731f, 2.4316974f),
		new TGCubicTo(1.5362506f, 2.4107287f, 1.6261132f, 2.3328474f, 1.6710457f, 2.1770837f),
		new TGCubicTo(1.7009994f, 2.0752387f, 1.7159768f, 1.9374487f, 1.7129818f, 1.7457399f),
		new TGCubicTo(1.7099856f, 1.610945f, 1.7039956f, 1.5450449f, 1.6860231f, 1.47615f),
		new TGCubicTo(1.6500769f, 1.3323689f, 1.5751907f, 1.2604775f, 1.4433919f, 1.2455013f),
		new TGCubicTo(1.3445419f, 1.2335188f, 1.2636644f, 1.2604775f, 1.1947694f, 1.3263776f),
		new TGCubicTo(1.1258744f, 1.3982687f, 1.0839381f, 1.5001137f, 1.0749519f, 1.6229262f),
		new TGCubicTo(1.0719569f, 1.6798398f, 1.0689607f, 1.6828363f, 1.0629693f, 1.6948175f),
		new TGCubicTo(1.0539831f, 1.7038038f, 1.0360106f, 1.71279f, 1.0210332f, 1.71279f),
		new TGCubicTo(1.0060569f, 1.71279f, 0.9850881f, 1.7008088f, 0.97909814f, 1.6918225f),
		new TGCubicTo(0.97310686f, 1.6828363f, 0.9701106f, 1.6768451f, 0.9701106f, 1.6379037f),
		new TGCubicTo(0.9641206f, 1.5180862f, 0.9401569f, 1.4282224f, 0.8922294f, 1.3233826f),
		new TGCubicTo(0.8562844f, 1.2514913f, 0.79637563f, 1.170615f, 0.74245685f, 1.1166964f),
		new TGLineTo(0.7244844f, 1.1017187f),
		new TGLineTo(0.7244844f, 1.7996588f),
		new TGCubicTo(0.7244844f, 2.5784738f, 0.72748065f, 2.5095787f, 0.7035169f, 2.5275512f),
		new TGCubicTo(0.6945306f, 2.5335424f, 0.6915344f, 2.5335424f, 0.6495981f, 2.5335424f),
		new TGCubicTo(0.6076619f, 2.5335424f, 0.6046669f, 2.5335424f, 0.59568065f, 2.5275512f),
		new TGCubicTo(0.5896894f, 2.52156f, 0.58369815f, 2.51557f, 0.58070314f, 2.5095787f),
		new TGCubicTo(0.57471186f, 2.4975975f, 0.57471186f, 2.386765f, 0.57471186f, 1.03582f),
		new TGLineTo(0.5777081f, -0.42895374f),
		new TGLineTo(0.58369815f, -0.43793997f),
		new TGCubicTo(0.5896894f, -0.44392997f, 0.59568065f, -0.44992122f, 0.6016719f, -0.45291623f)
	);
	
	private static final TGPaintCommand NEUTRAL_MODEL = new TGPaintModel(
		new TGMoveTo(0.0f, 1.0f),
		new TGLineTo(0.0f, 3.0f),
		new TGLineTo(0.5f, 3.0f),
		new TGLineTo(0.5f, 1.0f),
		new TGMoveTo(1.0f, 1.0f),
		new TGLineTo(1.0f, 3.0f),
		new TGLineTo(1.5f, 3.0f),
		new TGLineTo(1.5f, 1.0f)
	);
	
	public static void paintTreble(UIPainter painter, float x, float y,float scale){
		TREBLE_MODEL.paint(painter, x, y, scale);
	}
	
	public static void paintBass(UIPainter painter, float x, float y,float scale){
		BASS_MODEL.paint(painter, x, y, scale);
	}
	
	public static void paintAlto(UIPainter painter, float x, float y,float scale){
		ALTO_MODEL.paint(painter, x, y, scale);
	}
	
	public static void paintTenor(UIPainter painter, float x, float y,float scale){
		TENOR_MODEL.paint(painter, x, y, scale);
	}

	public static void paintNeutral(UIPainter painter, float x, float y, float scale) {
		NEUTRAL_MODEL.paint(painter, x, y, scale);
	}
}
