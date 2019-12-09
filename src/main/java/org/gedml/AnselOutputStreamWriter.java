package org.gedml;

import java.io.*;

/*
* org.lm.gedml.AnselOutputStreamWriter
* This class produces an output stream of bytes representing
* ANSEL-encoded characters, from UNICODE characters supplied as
* input.
* @Author: mhkay@iclway.co.uk
* @Version: 20 May 1998
* 20 May 1998: conversion tables updated with input from John Cowan
* Nov 2011: conversion tables updated again based upon
* http://www.heiner-eichmann.de/gedcom/oldansset.htm and http://lcweb2.loc.gov/diglib/codetables/45.html
*/
public class AnselOutputStreamWriter extends OutputStreamWriter
{
    private OutputStream output;

    public AnselOutputStreamWriter(OutputStream out)
    throws IOException
    {
        super(out);
        output = out;
    }

    /*
    * Write one UNICODE character
    */

    public void write(int c) throws IOException
    {

        int ansel;
        if (c<128) output.write(c);
        else {
          ansel = convert(c);
          if (ansel < 256) {
            output.write(ansel);
          } else {
            output.write(ansel / 256);
            output.write(ansel % 256);
          }
        }
    }

    /*
    * Write part of an array of UNICODE characters
    */

    public void write(char cbuf[], int off, int len)
                     throws IOException
    {
        for (int i=off; i<off+len; i++) {
            write(cbuf[i]);
        }
    }

    /*
    * Write a string of UNICODE characters
    */

    public void write(String s)
                     throws IOException
    {
        for (int i=0; i<s.length(); i++) {
            write((int)s.charAt(i));
        }
    }

    /*
    * Determine the character code in use
    */

    public String getEncoding() {
        return "ANSEL";
    }

    /*
    * Conversion table for UNICODE to Ansel
    */

    private int convert( int unicode )
    {
      switch(unicode) {

        case 0x00A1: return 0xC6;  //  inverted exclamation mark
        case 0x00A3: return 0xB9;  //  pound sign
        case 0x00A8: return 0xE820;  //  space with diaeresis
        case 0x00A9: return 0xC3;  //  copyright sign
        case 0x00AE: return 0xAA;  //  registered trade mark sign
        case 0x00B0: return 0xC0;  //  degree sign, ring above
        case 0x00B1: return 0xAB;  //  plus-minus sign
        case 0x00B7: return 0xA8;  //  middle dot
        case 0x00B8: return 0xF020;  //  cedilla
        case 0x00BF: return 0xC5;  //  inverted question mark
        case 0x00C0: return 0xE141;  //  capital A with grave accent
        case 0x00C1: return 0xE241;  //  capital A with acute accent
        case 0x00C2: return 0xE341;  //  capital A with circumflex accent
        case 0x00C3: return 0xE441;  //  capital A with tilde
        case 0x00C4: return 0xE841;  //  capital A with diaeresis
        case 0x00C5: return 0xEA41;  //  capital A with ring above
        case 0x00C6: return 0xA5;  //  capital diphthong A with E
        case 0x00C7: return 0xF043;  //  capital C with cedilla
        case 0x00C8: return 0xE145;  //  capital E with grave accent
        case 0x00C9: return 0xE245;  //  capital E with acute accent
        case 0x00CA: return 0xE345;  //  capital E with circumflex accent
        case 0x00CB: return 0xE845;  //  capital E with diaeresis
        case 0x00CC: return 0xE149;  //  capital I with grave accent
        case 0x00CD: return 0xE249;  //  capital I with acute accent
        case 0x00CE: return 0xE349;  //  capital I with circumflex accent
        case 0x00CF: return 0xE849;  //  capital I with diaeresis
        case 0x00D0: return 0xA3;  //  capital icelandic letter Eth
        case 0x00D1: return 0xE44E;  //  capital N with tilde
        case 0x00D2: return 0xE14F;  //  capital O with grave accent
        case 0x00D3: return 0xE24F;  //  capital O with acute accent
        case 0x00D4: return 0xE34F;  //  capital O with circumflex accent
        case 0x00D5: return 0xE44F;  //  capital O with tilde
        case 0x00D6: return 0xE84F;  //  capital O with diaeresis
        case 0x00D8: return 0xA2;  //  capital O with oblique stroke
        case 0x00D9: return 0xE155;  //  capital U with grave accent
        case 0x00DA: return 0xE255;  //  capital U with acute accent
        case 0x00DB: return 0xE355;  //  capital U with circumflex
        case 0x00DC: return 0xE855;  //  capital U with diaeresis
        case 0x00DD: return 0xE259;  //  capital Y with acute accent
        case 0x00DE: return 0xA4;  //  capital Icelandic letter Thorn
        case 0x00DF: return 0xCF;  //  small German letter sharp s
        case 0x00E0: return 0xE161;  //  small a with grave accent
        case 0x00E1: return 0xE261;  //  small a with acute accent
        case 0x00E2: return 0xE361;  //  small a with circumflex accent
        case 0x00E3: return 0xE461;  //  small a with tilde
        case 0x00E4: return 0xE861;  //  small a with diaeresis
        case 0x00E5: return 0xEA61;  //  small a with ring above
        case 0x00E6: return 0xB5;  //  small diphthong a with e
        case 0x00E7: return 0xF063;  //  small c with cedilla
        case 0x00E8: return 0xE165;  //  small e with grave accent
        case 0x00E9: return 0xE265;  //  small e with acute accent
        case 0x00EA: return 0xE365;  //  small e with circumflex accent
        case 0x00EB: return 0xE865;  //  small e with diaeresis
        case 0x00EC: return 0xE169;  //  small i with grave accent
        case 0x00ED: return 0xE269;  //  small i with acute accent
        case 0x00EE: return 0xE369;  //  small i with circumflex accent
        case 0x00EF: return 0xE869;  //  small i with diaeresis
        case 0x00F0: return 0xBA;  //  small Icelandic letter Eth
        case 0x00F1: return 0xE46E;  //  small n with tilde
        case 0x00F2: return 0xE16F;  //  small o with grave accent
        case 0x00F3: return 0xE26F;  //  small o with acute accent
        case 0x00F4: return 0xE36F;  //  small o with circumflex accent
        case 0x00F5: return 0xE46F;  //  small o with tilde
        case 0x00F6: return 0xE86F;  //  small o with diaeresis
        case 0x00F8: return 0xB2;  //  small o with oblique stroke
        case 0x00F9: return 0xE175;  //  small u with grave accent
        case 0x00FA: return 0xE275;  //  small u with acute accent
        case 0x00FB: return 0xE375;  //  small u with circumflex
        case 0x00FC: return 0xE875;  //  small u with diaeresis
        case 0x00FD: return 0xE279;  //  small y with acute accent
        case 0x00FE: return 0xB4;  //  small Icelandic letter Thorn
        case 0x00FF: return 0xE879;  //  small y with diaeresis
        case 0x0100: return 0xE541;  //  capital a with macron
        case 0x0101: return 0xE561;  //  small a with macron
        case 0x0102: return 0xE641;  //  capital A with breve
        case 0x0103: return 0xE661;  //  small a with breve
        case 0x0104: return 0xF141;  //  capital A with ogonek
        case 0x0105: return 0xF161;  //  small a with ogonek
        case 0x0106: return 0xE243;  //  capital C with acute accent
        case 0x0107: return 0xE263;  //  small c with acute accent
        case 0x0108: return 0xE343;  //  capital c with circumflex
        case 0x0109: return 0xE363;  //  small c with circumflex
        case 0x010A: return 0xE743;  //  capital c with dot above
        case 0x010B: return 0xE763;  //  small c with dot above
        case 0x010C: return 0xE943;  //  capital C with caron
        case 0x010D: return 0xE963;  //  small c with caron
        case 0x010E: return 0xE944;  //  capital D with caron
        case 0x010F: return 0xE964;  //  small d with caron
        case 0x0110: return 0xA3;  //  capital D with stroke
        case 0x0111: return 0xB3;  //  small D with stroke
        case 0x0112: return 0xE545;  //  capital e with macron
        case 0x0113: return 0xE565;  //  small e with macron
        case 0x0114: return 0xE645;  //  capital e with breve
        case 0x0115: return 0xE665;  //  small e with breve
        case 0x0116: return 0xE745;  //  capital e with dot above
        case 0x0117: return 0xE765;  //  small e with dot above
        case 0x0118: return 0xF145;  //  capital E with ogonek
        case 0x0119: return 0xF165;  //  small e with ogonek
        case 0x011A: return 0xE945;  //  capital E with caron
        case 0x011B: return 0xE965;  //  small e with caron
        case 0x011C: return 0xE347;  //  capital g with circumflex
        case 0x011D: return 0xE367;  //  small g with circumflex
        case 0x011E: return 0xE647;  //  capital g with breve
        case 0x011F: return 0xE667;  //  small g with breve
        case 0x0120: return 0xE747;  //  capital g with dot above
        case 0x0121: return 0xE767;  //  small g with dot above
        case 0x0122: return 0xF047;  //  capital g with cedilla
        case 0x0123: return 0xF067;  //  small g with cedilla
        case 0x0124: return 0xE348;  //  capital h with circumflex
        case 0x0125: return 0xE368;  //  small h with circumflex
        case 0x0128: return 0xE449;  //  capital i with tilde
        case 0x0129: return 0xE469;  //  small i with tilde
        case 0x012A: return 0xE549;  //  capital i with macron
        case 0x012B: return 0xE569;  //  small i with macron
        case 0x012C: return 0xE649;  //  capital i with breve
        case 0x012D: return 0xE669;  //  small i with breve
        case 0x012E: return 0xF149;  //  capital i with ogonek
        case 0x012F: return 0xF169;  //  small i with ogonek
        case 0x0130: return 0xE749;  //  capital i with dot above
        case 0x0131: return 0xB8;  //  small dotless i
        case 0x0134: return 0xE34A;  //  capital j with circumflex
        case 0x0135: return 0xE36A;  //  small j with circumflex
        case 0x0136: return 0xF04B;  //  capital k with cedilla
        case 0x0137: return 0xF06B;  //  small k with cedilla
        case 0x0139: return 0xE24C;  //  capital L with acute accent
        case 0x013A: return 0xE26C;  //  small l with acute accent
        case 0x013B: return 0xF04C;  //  capital l with cedilla
        case 0x013C: return 0xF06C;  //  small l with cedilla
        case 0x013D: return 0xE94C;  //  capital L with caron
        case 0x013E: return 0xE96C;  //  small l with caron
        case 0x0141: return 0xA1;  //  capital L with stroke
        case 0x0142: return 0xB1;  //  small l with stroke
        case 0x0143: return 0xE24E;  //  capital N with acute accent
        case 0x0144: return 0xE26E;  //  small n with acute accent
        case 0x0145: return 0xF04E;  //  capital n with cedilla
        case 0x0146: return 0xF06E;  //  small n with cedilla
        case 0x0147: return 0xE94E;  //  capital N with caron
        case 0x0148: return 0xE96E;  //  small n with caron
        case 0x014C: return 0xE54F;  //  capital o with macron
        case 0x014D: return 0xE56F;  //  small o with macron
        case 0x014E: return 0xE64F;  //  capital o with breve
        case 0x014F: return 0xE66F;  //  small o with breve
        case 0x0150: return 0xEE4F;  //  capital O with double acute
        case 0x0151: return 0xEE6F;  //  small o with double acute
        case 0x0152: return 0xA6;  //  capital ligature OE
        case 0x0153: return 0xB6;  //  small ligature OE
        case 0x0154: return 0xE252;  //  capital R with acute accent
        case 0x0155: return 0xE272;  //  small r with acute accent
        case 0x0156: return 0xF052;  //  capital r with cedilla
        case 0x0157: return 0xF072;  //  small r with cedilla
        case 0x0158: return 0xE952;  //  capital R with caron
        case 0x0159: return 0xE972;  //  small r with caron
        case 0x015A: return 0xE253;  //  capital S with acute accent
        case 0x015B: return 0xE273;  //  small s with acute accent
        case 0x015C: return 0xE353;  //  capital s with circumflex
        case 0x015D: return 0xE373;  //  small s with circumflex
        case 0x015E: return 0xF053;  //  capital S with cedilla
        case 0x015F: return 0xF073;  //  small s with cedilla
        case 0x0160: return 0xE953;  //  capital S with caron
        case 0x0161: return 0xE973;  //  small s with caron
        case 0x0162: return 0xF054;  //  capital T with cedilla
        case 0x0163: return 0xF074;  //  small t with cedilla
        case 0x0164: return 0xE954;  //  capital T with caron
        case 0x0165: return 0xE974;  //  small t with caron
        case 0x0168: return 0xE455;  //  capital u with tilde
        case 0x0169: return 0xE475;  //  small u with tilde
        case 0x016A: return 0xE555;  //  capital u with macron
        case 0x016B: return 0xE575;  //  small u with macron
        case 0x016C: return 0xE655;  //  capital u with breve
        case 0x016D: return 0xE675;  //  small u with breve
        case 0x016E: return 0xEAAD;  //  capital U with ring above
        case 0x016F: return 0xEA75;  //  small u with ring above
        case 0x0170: return 0xEE55;  //  capital U with double acute
        case 0x0171: return 0xEE75;  //  small u with double acute
        case 0x0172: return 0xF155;  //  capital u with ogonek
        case 0x0173: return 0xF175;  //  small u with ogonek
        case 0x0174: return 0xE357;  //  capital w with circumflex
        case 0x0175: return 0xE377;  //  small w with circumflex
        case 0x0176: return 0xE359;  //  capital y with circumflex
        case 0x0177: return 0xE379;  //  small y with circumflex
        case 0x0178: return 0xE859;  //  capital y with diaeresis
        case 0x0179: return 0xE25A;  //  capital Z with acute accent
        case 0x017A: return 0xE27A;  //  small z with acute accent
        case 0x017B: return 0xE75A;  //  capital Z with dot above
        case 0x017C: return 0xE77A;  //  small z with dot above
        case 0x017D: return 0xE95A;  //  capital Z with caron
        case 0x017E: return 0xE97A;  //  small z with caron
        case 0x01A0: return 0xAC;  //  capital O with horn
        case 0x01A1: return 0xBC;  //  small o with horn
        case 0x01AF: return 0xAD;  //  capital U with horn
        case 0x01B0: return 0xBD;  //  small u with horn
        case 0x01CD: return 0xE941;  //  capital a with caron
        case 0x01CE: return 0xE961;  //  small a with caron
        case 0x01CF: return 0xE949;  //  capital i with caron
        case 0x01D0: return 0xE969;  //  small i with caron
        case 0x01D1: return 0xE94F;  //  capital o with caron
        case 0x01D2: return 0xE96F;  //  small o with caron
        case 0x01D3: return 0xE955;  //  capital u with caron
        case 0x01D4: return 0xE975;  //  small u with caron
        case 0x01E2: return 0xE5A5;  //  capital ae with macron
        case 0x01E3: return 0xE5B5;  //  small ae with macron
        case 0x01E6: return 0xE947;  //  capital g with caron
        case 0x01E7: return 0xE967;  //  small g with caron
        case 0x01E8: return 0xE94B;  //  capital k with caron
        case 0x01E9: return 0xE96B;  //  small k with caron
        case 0x01EA: return 0xF14F;  //  capital o with ogonek
        case 0x01EB: return 0xF16F;  //  small o with ogonek
        case 0x01F0: return 0xE96A;  //  small j with caron
        case 0x01F4: return 0xE247;  //  capital g with acute
        case 0x01F5: return 0xE267;  //  small g with acute
        case 0x01FC: return 0xE2A5;  //  capital ae with acute
        case 0x01FD: return 0xE2B5;  //  small ae with acute
        case 0x02B9: return 0xA7;  //  modified letter prime
        case 0x02BA: return 0xB7;  //  modified letter double prime
        case 0x02BC: return 0xED20;  //  space with high comma
        case 0x02BE: return 0xAE;  //  modifier letter right half ring
        case 0x02BF: return 0xB0;  //  modifier letter left half ring
        case 0x02C0: return 0xE020;  //  space with low-rising tone mark
        case 0x02C6: return 0xE320;  //  space with circumflex accent
        case 0x02C7: return 0xE920;  //  space with caron
        case 0x02C9: return 0xE520;  //  space with macron
        case 0x02CA: return 0xE220;  //  space with acute accent
        case 0x02CB: return 0xE120;  //  space with grave accent
        case 0x02D8: return 0xE620;  //  space with breve
        case 0x02D9: return 0xE720;  //  space with dot above
        case 0x02DA: return 0xEA20;  //  space with ring above
        case 0x02DB: return 0xF120;  //  space with ogonek
        case 0x02DC: return 0xE420;  //  space with tilde
        case 0x02DD: return 0xEE20;  //  space with double acute accent
        case 0x0300: return 0xE1;  //  grave accent
        case 0x0301: return 0xE2;  //  acute accent
        case 0x0302: return 0xE3;  //  circumflex accent
        case 0x0303: return 0xE4;  //  tilde
        case 0x0304: return 0xE5;  //  combining macron
        case 0x0306: return 0xE6;  //  breve
        case 0x0307: return 0xE7;  //  dot above
        case 0x0309: return 0xE0;  //  hook above
        case 0x030A: return 0xEA;  //  ring above
        case 0x030B: return 0xEE;  //  double acute accent
        case 0x030C: return 0xE9;  //  caron
        case 0x0310: return 0xEF;  //  candrabindu
        case 0x0313: return 0xFE;  //  comma above
        case 0x0315: return 0xED;  //  comma above right
        case 0x031C: return 0xF8;  //  combining half ring below
        case 0x0323: return 0xF2;  //  dot below
        case 0x0324: return 0xF3;  //  diaeresis below
        case 0x0325: return 0xF4;  //  ring below
        case 0x0326: return 0xF7;  //  comma below
        case 0x0327: return 0xF0;  //  combining cedilla
        case 0x0328: return 0xF1;  //  ogonek
        case 0x032E: return 0xF9;  //  breve below
        case 0x0332: return 0xF6;  //  low line (= line below?)
        case 0x0333: return 0xF5;  //  double low line
        case 0x1E00: return 0xF441;  //  capital a with ring below
        case 0x1E01: return 0xF461;  //  small a with ring below
        case 0x1E02: return 0xE742;  //  capital b with dot above
        case 0x1E03: return 0xE762;  //  small b with dot above
        case 0x1E04: return 0xF242;  //  capital b with dot below
        case 0x1E05: return 0xF262;  //  small b with dot below
        case 0x1E0A: return 0xE744;  //  capital d with dot above
        case 0x1E0B: return 0xE764;  //  small d with dot above
        case 0x1E0C: return 0xF244;  //  capital d with dot below
        case 0x1E0D: return 0xF264;  //  small d with dot below
        case 0x1E10: return 0xF044;  //  capital d with cedilla
        case 0x1E11: return 0xF064;  //  small d with cedilla
        case 0x1E1E: return 0xE746;  //  capital f with dot above
        case 0x1E1F: return 0xE766;  //  small f with dot above
        case 0x1E20: return 0xE547;  //  capital g with macron
        case 0x1E21: return 0xE567;  //  small g with macron
        case 0x1E22: return 0xE748;  //  capital h with dot above
        case 0x1E23: return 0xE768;  //  small h with dot above
        case 0x1E24: return 0xF248;  //  capital h with dot below
        case 0x1E25: return 0xF268;  //  small h with dot below
        case 0x1E26: return 0xE848;  //  capital h with diaeresis
        case 0x1E27: return 0xE868;  //  small h with diaeresis
        case 0x1E28: return 0xF048;  //  capital h with cedilla
        case 0x1E29: return 0xF068;  //  small h with cedilla
        case 0x1E2A: return 0xF948;  //  capital h with breve below
        case 0x1E2B: return 0xF968;  //  small h with breve below
        case 0x1E30: return 0xE24B;  //  capital k with acute
        case 0x1E31: return 0xE26B;  //  small k with acute
        case 0x1E32: return 0xF24B;  //  capital k with dot below
        case 0x1E33: return 0xF26B;  //  small k with dot below
        case 0x1E36: return 0xF24C;  //  capital l with dot below
        case 0x1E37: return 0xF26C;  //  small l with dot below
        case 0x1E3E: return 0xE24D;  //  capital m with acute
        case 0x1E3F: return 0xE26D;  //  small m with acute
        case 0x1E40: return 0xE74D;  //  capital m with dot above
        case 0x1E41: return 0xE76D;  //  small m with dot above
        case 0x1E42: return 0xF24D;  //  capital m with dot below
        case 0x1E43: return 0xF26D;  //  small m with dot below
        case 0x1E44: return 0xE74E;  //  capital n with dot above
        case 0x1E45: return 0xE76E;  //  small n with dot above
        case 0x1E46: return 0xF24E;  //  capital n with dot below
        case 0x1E47: return 0xF26E;  //  small n with dot below
        case 0x1E54: return 0xE250;  //  capital p with acute
        case 0x1E55: return 0xE270;  //  small p with acute
        case 0x1E56: return 0xE750;  //  capital p with dot above
        case 0x1E57: return 0xE770;  //  small p with dot above
        case 0x1E58: return 0xE752;  //  capital r with dot above
        case 0x1E59: return 0xE772;  //  small r with dot above
        case 0x1E5A: return 0xF252;  //  capital r with dot below
        case 0x1E5B: return 0xF272;  //  small r with dot below
        case 0x1E60: return 0xE753;  //  capital s with dot above
        case 0x1E61: return 0xE773;  //  small s with dot above
        case 0x1E62: return 0xF253;  //  capital s with dot below
        case 0x1E63: return 0xF273;  //  small s with dot below
        case 0x1E6A: return 0xE754;  //  capital t with dot above
        case 0x1E6B: return 0xE774;  //  small t with dot above
        case 0x1E6C: return 0xF254;  //  capital t with dot below
        case 0x1E6D: return 0xF274;  //  small t with dot below
        case 0x1E72: return 0xF355;  //  capital u with diaeresis below
        case 0x1E73: return 0xF375;  //  small u with diaeresis below
        case 0x1E7C: return 0xE456;  //  capital v with tilde
        case 0x1E7D: return 0xE476;  //  small v with tilde
        case 0x1E7E: return 0xF256;  //  capital v with dot below
        case 0x1E7F: return 0xF276;  //  small v with dot below
        case 0x1E80: return 0xE157;  //  capital w with grave
        case 0x1E81: return 0xE177;  //  small w with grave
        case 0x1E82: return 0xE257;  //  capital w with acute
        case 0x1E83: return 0xE277;  //  small w with acute
        case 0x1E84: return 0xE857;  //  capital w with diaeresis
        case 0x1E85: return 0xE877;  //  small w with diaeresis
        case 0x1E86: return 0xE757;  //  capital w with dot above
        case 0x1E87: return 0xE777;  //  small w with dot above
        case 0x1E88: return 0xF257;  //  capital w with dot below
        case 0x1E89: return 0xF277;  //  small w with dot below
        case 0x1E8A: return 0xE758;  //  capital x with dot above
        case 0x1E8B: return 0xE778;  //  small x with dot above
        case 0x1E8C: return 0xE858;  //  capital x with diaeresis
        case 0x1E8D: return 0xE878;  //  small x with diaeresis
        case 0x1E8E: return 0xE759;  //  capital y with dot above
        case 0x1E8F: return 0xE779;  //  small y with dot above
        case 0x1E90: return 0xE35A;  //  capital z with circumflex
        case 0x1E91: return 0xE37A;  //  small z with circumflex
        case 0x1E92: return 0xF25A;  //  capital z with dot below
        case 0x1E93: return 0xF27A;  //  small z with dot below
        case 0x1E97: return 0xE874;  //  small t with diaeresis
        case 0x1E98: return 0xEA77;  //  small w with ring above
        case 0x1E99: return 0xEA79;  //  small y with ring above
        case 0x1EA0: return 0xF241;  //  capital a with dot below
        case 0x1EA1: return 0xF261;  //  small a with dot below
        case 0x1EA2: return 0xE041;  //  capital a with hook above
        case 0x1EA3: return 0xE061;  //  small a with hook above
        case 0x1EB8: return 0xF245;  //  capital e with dot below
        case 0x1EB9: return 0xF265;  //  small e with dot below
        case 0x1EBA: return 0xE045;  //  capital e with hook above
        case 0x1EBB: return 0xE065;  //  small e with hook above
        case 0x1EBC: return 0xE445;  //  capital e with tilde
        case 0x1EBD: return 0xE465;  //  small e with tilde
        case 0x1EC8: return 0xE049;  //  capital i with hook above
        case 0x1EC9: return 0xE069;  //  small i with hook above
        case 0x1ECA: return 0xF249;  //  capital i with dot below
        case 0x1ECB: return 0xF269;  //  small i with dot below
        case 0x1ECC: return 0xF24F;  //  capital o with dot below
        case 0x1ECD: return 0xF26F;  //  small o with dot below
        case 0x1ECE: return 0xE04F;  //  capital o with hook above
        case 0x1ECF: return 0xE06F;  //  small o with hook above
        case 0x1EE4: return 0xF255;  //  capital u with dot below
        case 0x1EE5: return 0xF275;  //  small u with dot below
        case 0x1EE6: return 0xE055;  //  capital u with hook above
        case 0x1EE7: return 0xE075;  //  small u with hook above
        case 0x1EF2: return 0xE159;  //  capital y with grave
        case 0x1EF3: return 0xE179;  //  small y with grave
        case 0x1EF4: return 0xF259;  //  capital y with dot below
        case 0x1EF5: return 0xF279;  //  small y with dot below
        case 0x1EF6: return 0xE059;  //  capital y with hook above
        case 0x1EF7: return 0xE079;  //  small y with hook above
        case 0x1EF8: return 0xE459;  //  capital y with tilde
        case 0x1EF9: return 0xE479;  //  small y with tilde
        case 0x200C: return 0x8E;    //  zero width non-joiner
        case 0x200D: return 0x8D;    //  zero width joiner
        case 0x2017: return 0xF520;  //  space with double line below
        case 0x2113: return 0xC1;  //  script small l
        case 0x2117: return 0xC2;  //  sound recording copyright
        case 0x266D: return 0xA9;  //  music flat sign
        case 0x266F: return 0xC4;  //  music sharp sign
        case 0xFE20: return 0xEB;  //  ligature left half
        case 0xFE21: return 0xEC;  //  ligature right half
        case 0xFE22: return 0xFA;  //  double tilde left half
        case 0xFE23: return 0xFB;  //  double tilde right half
        // WeRelate added
        //case 0x00FC: return 0x81;  // ü - umlaught -- duplicate
        case 0x0098: return 0x88;  // non-sort begin
        case 0x009C: return 0x89;  // non-sort end
        case 0x0027: return 0x92;  // apostrophe
        //case 0x00DF: return 0xC7;  //  eszett symbol -- duplicate
        case 0x201C: return 0x93;    // left double quotation mark (not in the standard)
        case 0x201D: return 0x94;    // right double quotation mark (not in the standard)
        case 0x20AC: return 0xC8;  //  euro sign
        case 0x0308: return 0xE8;  //  umlaut, diaeresis
        // FamilySearch Extensions
        case 0x0338: return 0xFC20;  // space with diagonal stroke
        case 0x00A0: return 0xD959;  // no-break space
        case 0x00A2: return 0xD921;  // cent sign
        case 0x00A4: return 0xD925;  // currency sign
        case 0x00A5: return 0xD922;  // yen sign
        case 0x00A6: return 0xD945;  // broken vertical bar
        case 0x00A7: return 0xD947;  // section sign
        case 0x00AA: return 0xD941;  // feminine ordinal indicator
        case 0x00AB: return 0xD943;  // left-pointing double angle quotation mark
        case 0x00AC: return 0xD968;  // not sign
        case 0x00AD: return 0xD977;  // soft hyphen
        case 0x00B2: return 0xD932;  // superscript two
        case 0x00B3: return 0xD933;  // superscript three
        case 0x00B5: return 0xD95A;  // micro sign
        case 0x00B6: return 0xD946;  // pilcrow/paragraph sign
        case 0x00B9: return 0xD931;  // superscript one
        case 0x00BA: return 0xD942;  // masculine ordinal indicator
        case 0x00BB: return 0xD944;  // right-pointing double angle quotation mark
        case 0x00BC: return 0xD962;  // vulgar fraction one quarter
        case 0x00BD: return 0xD961;  // vulgar fraction one half
        case 0x00BE: return 0xD963;  // vulgar fraction three quarters
        case 0x00D7: return 0xD96A;  // multiplication sign
        case 0x00F7: return 0xD96D;  // division sign
        case 0x0192: return 0xD924;  // florin currency sign
        case 0x023A: return 0xFC41;  // capital A with diagonal stroke
        case 0x023B: return 0xFC43;  // capital C with diagonal stroke
        case 0x023C: return 0xFC63;  // small c with diagonal stroke
        case 0x023E: return 0xFC54;  // capital T with diagonal stroke
        case 0x0246: return 0xFC45;  // capital E with diagonal stroke
        case 0x0247: return 0xFC65;  // small e with diagonal stroke
        case 0x0391: return 0xD841;  // Greek capital letter alpha
        case 0x0392: return 0xD842;  // Greek capital letter beta
        case 0x0393: return 0xD847;  // Greek capital letter gamma
        case 0x0394: return 0xD844;  // Greek capital letter delta
        case 0x0395: return 0xD845;  // Greek capital letter epsilon
        case 0x0396: return 0xD85A;  // Greek capital letter zeta
        case 0x0397: return 0xD857;  // Greek capital letter eta
        case 0x0398: return 0xD856;  // Greek capital letter theta
        case 0x0399: return 0xD849;  // Greek capital letter iota
        case 0x039A: return 0xD84B;  // Greek capital letter kappa
        case 0x039B: return 0xD84C;  // Greek capital letter lamda
        case 0x039C: return 0xD84D;  // Greek capital letter mu
        case 0x039D: return 0xD84E;  // Greek capital letter nu
        case 0x039E: return 0xD858;  // Greek capital letter xi
        case 0x039F: return 0xD84F;  // Greek capital letter omicron
        case 0x03A0: return 0xD850;  // Greek capital letter pi
        case 0x03A1: return 0xD852;  // Greek capital letter rho
        case 0x03A3: return 0xD853;  // Greek capital letter sigma
        case 0x03A4: return 0xD854;  // Greek capital letter tau
        case 0x03A5: return 0xD843;  // Greek capital letter upsilon
        case 0x03A6: return 0xD846;  // Greek capital letter phi
        case 0x03A7: return 0xD851;  // Greek capital letter chi
        case 0x03A8: return 0xD848;  // Greek capital letter psi
        case 0x03A9: return 0xD855;  // Greek capital letter omega
        case 0x03B1: return 0xD861;  // Greek small letter alpha
        case 0x03B2: return 0xD862;  // Greek small letter beta
        case 0x03B3: return 0xD867;  // Greek small letter gamma
        case 0x03B4: return 0xD864;  // Greek small letter delta
        case 0x03B5: return 0xD865;  // Greek small letter epsilon
        case 0x03B6: return 0xD87A;  // Greek small letter zeta
        case 0x03B7: return 0xD877;  // Greek small letter eta
        case 0x03B8: return 0xD876;  // Greek small letter theta
        case 0x03B9: return 0xD869;  // Greek small letter iota
        case 0x03BA: return 0xD86B;  // Greek small letter kappa
        case 0x03BB: return 0xD86C;  // Greek small letter lamda
        case 0x03BC: return 0xD86D;  // Greek small letter mu
        case 0x03BD: return 0xD86E;  // Greek small letter nu
        case 0x03BE: return 0xD878;  // Greek small letter xi
        case 0x03BF: return 0xD86F;  // Greek small letter omicron
        case 0x03C0: return 0xD870;  // Greek small letter pi
        case 0x03C1: return 0xD872;  // Greek small letter rho
        case 0x03C2: return 0xD824;  // Greek small letter final sigma
        case 0x03C3: return 0xD873;  // Greek small letter sigma
        case 0x03C4: return 0xD874;  // Greek small letter tau
        case 0x03C5: return 0xD863;  // Greek small letter upsilon
        case 0x03C6: return 0xD866;  // Greek small letter phi
        case 0x03C7: return 0xD871;  // Greek small letter chi
        case 0x03C8: return 0xD868;  // Greek small letter psi
        case 0x03C9: return 0xD875;  // Greek small letter omega
        case 0x0E3F: return 0xD920;  // Thai currency symbol baht
        case 0x2013: return 0xD958;  // en dash
        case 0x2014: return 0xD976;  // em dash
        case 0x2018: return 0xD974;  // left single quotation mark
        case 0x2019: return 0xD975;  // right single quotation mark
        case 0x201A: return 0xD955;  // single low-9 quotation mark
        //case 0x201C: return 0xD953;  // left double quotation mark (conflicts with WeRelate)
        //case 0x201D: return 0xD954;  // right double quotation mark (conflicts with WeRelate)
        case 0x201E: return 0xD956;  // double low-9 quotation mark
        case 0x2020: return 0xD949;  // dagger
        case 0x2021: return 0xD94A;  // double dagger
        case 0x2022: return 0xD957;  // bullet
        case 0x2026: return 0xD952;  // horizontal ellipsis
        case 0x2030: return 0xD951;  // per mille sign
        case 0x2039: return 0xD94F;  // left-pointing single angle quotation mark
        case 0x203A: return 0xD950;  // right-pointing single angle quotation mark
        case 0x2044: return 0xD973;  // fraction/division slash
        case 0x20A0: return 0xD927;  // Euro-currency sign
        case 0x20A1: return 0xD928;  // colon sign
        case 0x20A2: return 0xD929;  // cruzeiro sign
        case 0x20A3: return 0xD92A;  // French franc sign
        case 0x20A4: return 0xD926;  // lira sign
        case 0x20A5: return 0xD92B;  // mill sign
        case 0x20A6: return 0xD92C;  // naira sign
        case 0x20A7: return 0xD923;  // peseta sign
        case 0x20A8: return 0xD92D;  // rupee sign
        case 0x20A9: return 0xD92E;  // won sign
        case 0x20AA: return 0xD92F;  // new sheqel sign
        case 0x20AB: return 0xD97C;  // dong sign
        //case 0x20AC: return 0xD930;  // Euro sign (conflicts with WeRelate)
        case 0x20AD: return 0xD979;  // kip sign
        case 0x20AE: return 0xD97A;  // tugrik sign
        case 0x20AF: return 0xD97B;  // drachma sign
        case 0x2122: return 0xD94C;  // trade mark sign
        case 0x2205: return 0xD965;  // empty set
        case 0x2208: return 0xD966;  // element of
        case 0x221A: return 0xD972;  // square root
        case 0x221E: return 0xD964;  // infinity
        case 0x2229: return 0xD967;  // intersection
        case 0x2248: return 0xD96E;  // almost equal to
        case 0x2261: return 0xD969;  // identical to
        case 0x2264: return 0xD96C;  // less-than or equal to
        case 0x2265: return 0xD96B;  // greater-than or equal to
        case 0x22C5: return 0xD96F;  // dot operator
        case 0x2310: return 0xD948;  // reversed not sign
        case 0x2320: return 0xD970;  // top half integral
        case 0x2321: return 0xD971;  // bottom half integral
        case 0x2500: return 0xD761;  // box drawings light horizontal
        case 0x2502: return 0xD762;  // box drawings light vertical
        case 0x250C: return 0xD766;  // box drawings light down and right
        case 0x2510: return 0xD763;  // box drawings light down and left
        case 0x2514: return 0xD765;  // box drawings light up and right
        case 0x2518: return 0xD764;  // box drawings light up and left
        case 0x251C: return 0xD769;  // box drawings light vertical and right
        case 0x2524: return 0xD767;  // box drawings light vertical and left
        case 0x252C: return 0xD76A;  // box drawings light down and horizontal
        case 0x2534: return 0xD768;  // box drawings light up and horizontal
        case 0x253C: return 0xD76B;  // box drawings light vertical and horizontal
        case 0x2550: return 0xD741;  // box drawings double horizontal
        case 0x2551: return 0xD742;  // box drawings double vertical
        case 0x2552: return 0xD76F;  // box drawings light single down and double right
        case 0x2553: return 0xD773;  // box drawings light double down and single right
        case 0x2554: return 0xD746;  // box drawings double down and right
        case 0x2555: return 0xD76C;  // box drawings light single down and double left
        case 0x2556: return 0xD770;  // box drawings light double down and single left
        case 0x2557: return 0xD743;  // box drawings double down and left
        case 0x2558: return 0xD76E;  // box drawings light single up and double right
        case 0x2559: return 0xD772;  // box drawings light double up and single right
        case 0x255A: return 0xD745;  // box drawings double up and right
        case 0x255B: return 0xD76D;  // box drawings light single up and double left
        case 0x255C: return 0xD771;  // box drawings light double up and single left
        case 0x255D: return 0xD744;  // box drawings double up and left
        case 0x255E: return 0xD752;  // box drawings single vertical and double right
        case 0x255F: return 0xD74E;  // box drawings double vertical and single right
        case 0x2560: return 0xD749;  // box drawings double vertical and right
        case 0x2561: return 0xD750;  // box drawings single vertical and double left
        case 0x2562: return 0xD74C;  // box drawings double vertical and single left
        case 0x2563: return 0xD747;  // box drawings double vertical and left
        case 0x2564: return 0xD74F;  // box drawings single down and double horizontal
        case 0x2565: return 0xD753;  // box drawings double down and single horizontal
        case 0x2566: return 0xD74A;  // box drawings double down and horizontal
        case 0x2567: return 0xD74D;  // box drawings single up and double horizontal
        case 0x2568: return 0xD751;  // box drawings double up and single horizontal
        case 0x2569: return 0xD748;  // box drawings double up and horizontal
        case 0x256A: return 0xD754;  // box drawings single vertical and double horizontal
        case 0x256B: return 0xD774;  // box drawings light double vertical and single horizontal
        case 0x256C: return 0xD74B;  // box drawings double vertical and horizontal
        case 0x2580: return 0xD776;  // upper half block
        case 0x2584: return 0xD778;  // lower half block
        case 0x2588: return 0xD758;  // full block
        case 0x258C: return 0xD775;  // left half block
        case 0x2590: return 0xD777;  // right half block
        case 0x2591: return 0xD755;  // light shade - 25%
        case 0x2592: return 0xD756;  // medium shade - 50%
        case 0x2593: return 0xD757;  // dark shade - 75%
        case 0x25AA: return 0xD779;  // black small square
        case 0x25AE: return 0xBF;    // black vertical rectangle
        case 0x25AF: return 0xBE;    // white vertical rectangle
        case 0x2C65: return 0xFC61;  // small a with diagonal stroke
        case 0x2C66: return 0xFC74;  // small t with diagonal stroke
        case 0xA742: return 0xFC4B;  // capital K with diagonal stroke
        case 0xA743: return 0xFC6B;  // small k with diagonal stroke
        case 0xA758: return 0xFC51;  // capital Q with diagonal stroke
        case 0xA759: return 0xFC71;  // small q with diagonal stroke
        case 0xA75E: return 0xFC56;  // capital V with diagonal stroke
        case 0xA75F: return 0xFC76;  // small v with diagonal stroke
        case 0xFB01: return 0xD94D;  // Latin small ligature fi
        case 0xFB02: return 0xD94E;  // Latin small ligature fl

        default: return 0xC5;     // if no match, use inverted '?'
      } //end switch

      /* Note: this conversion table is currently the exact inverse of that used in
      * ANSELInputStreamReader. Ideally it should also provide fallback conversion for
      * UNICODE characters that are never generated by ANSELInputStreamReader, e.g.
      * free-standing accents. For future work.
      */
    }


}
