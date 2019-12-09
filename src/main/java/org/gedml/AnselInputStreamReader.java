package org.gedml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
* org.gedml.AnselInputStreamReader
* This class reads an input stream of bytes representing
* ANSEL-encoded characters, and delivers a stream of UNICODE characters
* @Author: mhkay@iclway.co.uk
* @GedcomVersion: 20 May 1998
* 20 May 1998: conversion tables updated with input from John Cowan
* Nov 2011: conversion tables updated again based upon
* http://www.heiner-eichmann.de/gedcom/oldansset.htm and http://lcweb2.loc.gov/diglib/codetables/45.html
*/
public class AnselInputStreamReader extends InputStreamReader
{
    private InputStream input;
    private int pending;

    public AnselInputStreamReader(InputStream in)
            throws IOException
    {
        super(in);
        input = in;
        pending = input.read(); // we read one character ahead to cope
        // with non-spacing diacriticals
    }

    /*
    * Return one UNICODE character
    */

    public int read() throws IOException
    {
        int b = pending;
        if (b<0) return b;     // return EOF unchanged
        pending = input.read();
        if (b<128) return b;   // return ASCII characters unchanged

        // try to match two ansel chars if we can
        if (pending>0 && ((b>=0xE0 && b<=0xFF) || (b>=0xd7 && b<=0xd9))) {
            int u = convert2(b*256 + pending);
            if (u>0) {
                pending = input.read();
                return u;
            }
        }
        // else match one char
        return(convert1(b));
    }

    /*
    * Fill a supplied buffer with UNICODE characters
    */

    public int read(char cbuf[], int off, int len)
            throws IOException
    {
        if (pending<0) return -1;  // have already hit EOF
        for (int i=off; i<off+len; i++) {
            int c = read();
            if (c<0) {
                return i-off;
            }
            cbuf[off+i] = (char)c;
        }
        return len;
    }

    /*
    * Determine the character code in use
    */

    public String getEncoding() {
        return "ANSEL";
    }

    /*
    * Conversion table for ANSEL characters coded in one byte
    */

    private int convert1( int ansel )
    {
        switch(ansel) {
            case 0x8D: return 0x200D;  //  zero width joiner
            case 0x8E: return 0x200C;  //  zero width non-joiner
            case 0xA1: return 0x0141;  //  capital L with stroke
            case 0xA2: return 0x00D8;  //  capital O with oblique stroke
            // case 0xA3: return 0x0110;   capital D with stroke
            case 0xA3: return 0x00D0;  //  capital icelandic letter Eth
            case 0xA4: return 0x00DE;  //  capital Icelandic letter Thorn
            case 0xA5: return 0x00C6;  //  capital diphthong A with E
            case 0xA6: return 0x0152;  //  capital ligature OE
            case 0xA7: return 0x02B9;  //  modified letter prime
            case 0xA8: return 0x00B7;  //  middle dot
            case 0xA9: return 0x266D;  //  music flat sign
            case 0xAA: return 0x00AE;  //  registered trade mark sign
            case 0xAB: return 0x00B1;  //  plus-minus sign
            case 0xAC: return 0x01A0;  //  capital O with horn
            case 0xAD: return 0x01AF;  //  capital U with horn
            case 0xAE: return 0x02BE;  //  modifier letter right half ring
            case 0xB0: return 0x00B0;  //  modifier letter left half ring
            case 0xB1: return 0x0142;  //  small l with stroke
            case 0xB2: return 0x00F8;  //  small o with oblique stroke
            case 0xB3: return 0x0111;  //  small D with stroke
            case 0xB4: return 0x00FE;  //  small Icelandic letter Thorn
            case 0xB5: return 0x00E6;  //  small diphthong a with e
            case 0xB6: return 0x0153;  //  small ligature OE
            case 0xB7: return 0x02BA;  //  modified letter double prime
            case 0xB8: return 0x0131;  //  small dotless i
            case 0xB9: return 0x00A3;  //  pound sign
            case 0xBA: return 0x00F0;  //  small Icelandic letter Eth
            case 0xBC: return 0x01A1;  //  small o with horn
            case 0xBD: return 0x01B0;  //  small u with horn
            case 0xC0: return 0x00B0;  //  degree sign, ring above
            case 0xC1: return 0x2113;  //  script small l
            case 0xC2: return 0x2117;  //  sound recording copyright
            case 0xC3: return 0x00A9;  //  copyright sign
            case 0xC4: return 0x266F;  //  music sharp sign
            case 0xC5: return 0x00BF;  //  inverted question mark
            case 0xC6: return 0x00A1;  //  inverted exclamation mark
            case 0xCF: return 0x00DF;  //  small German letter sharp s
            case 0xE0: return 0x0309;  //  hook above
            case 0xE1: return 0x0300;  //  grave accent
            case 0xE2: return 0x0301;  //  acute accent
            case 0xE3: return 0x0302;  //  circumflex accent
            case 0xE4: return 0x0303;  //  tilde
            case 0xE5: return 0x0304;  //  combining macron
            case 0xE6: return 0x0306;  //  breve
            case 0xE7: return 0x0307;  //  dot above
            case 0xE9: return 0x030C;  //  caron
            case 0xEA: return 0x030A;  //  ring above
            case 0xEB: return 0xFE20;  //  ligature left half
            case 0xEC: return 0xFE21;  //  ligature right half
            case 0xED: return 0x0315;  //  comma above right
            case 0xEE: return 0x030B;  //  double acute accent
            case 0xEF: return 0x0310;  //  candrabindu
            case 0xF0: return 0x0327;  //  combining cedilla
            case 0xF1: return 0x0328;  //  ogonek
            case 0xF2: return 0x0323;  //  dot below
            case 0xF3: return 0x0324;  //  diaeresis below
            case 0xF4: return 0x0325;  //  ring below
            case 0xF5: return 0x0333;  //  double low line
            case 0xF6: return 0x0332;  //  low line (= line below?)
            case 0xF7: return 0x0326;  //  comma below
            case 0xF8: return 0x031C;  //  combining half ring below
            case 0xF9: return 0x032E;  //  breve below
            case 0xFA: return 0xFE22;  //  double tilde left half
            case 0xFB: return 0xFE23;  //  double tilde right half
            case 0xFE: return 0x0313;  //  comma above
            // WeRelate added
            case 0x81: return 0x00FC;  // ü - umlaught
            case 0x88: return 0x0098;  // non-sort begin
            case 0x89: return 0x009C;  // non-sort end
            case 0x92: return 0x0027;  // apostophe
            case 0x93: return 0x201C;  // left double quotation mark (not in the standard)
            case 0x94: return 0x201D;  // right double quotation mark (not in the standard)
            case 0xC7: return 0x00DF;  // eszett symbol
            case 0xC8: return 0x20AC;  // euro sign
            case 0xE8: return 0x0308;  // umlaut, diaresis
            // FamilySearch Extensions
            case 0xBE: return 0x25AF;  // white vertical rectangle
            case 0xBF: return 0x25AE;  // black vertical rectangle
            case 0xCD: return 0x0065;  // e in middle of line
            case 0xCE: return 0x006F;  // o in middle of line

            default: return 0xFFFD;     // if no match, use Unicode REPLACEMENT CHARACTER
        } //end switch
    }

    /*
    * Conversion table for ANSEL characters coded in two bytes
    */

    private int convert2( int ansel )
    {
        switch(ansel) {
            case 0xE020: return 0x02C0;  //  modifier letter glottal stop
            case 0xE041: return 0x1EA2;  //  capital a with hook above
            case 0xE045: return 0x1EBA;  //  capital e with hook above
            case 0xE049: return 0x1EC8;  //  capital i with hook above
            case 0xE04F: return 0x1ECE;  //  capital o with hook above
            case 0xE055: return 0x1EE6;  //  capital u with hook above
            case 0xE059: return 0x1EF6;  //  capital y with hook above
            case 0xE061: return 0x1EA3;  //  small a with hook above
            case 0xE065: return 0x1EBB;  //  small e with hook above
            case 0xE069: return 0x1EC9;  //  small i with hook above
            case 0xE06F: return 0x1ECF;  //  small o with hook above
            case 0xE075: return 0x1EE7;  //  small u with hook above
            case 0xE079: return 0x1EF7;  //  small y with hook above
            case 0xE120: return 0x02CB;  //  modifier letter grave accent
            case 0xE141: return 0x00C0;  //  capital A with grave accent
            case 0xE145: return 0x00C8;  //  capital E with grave accent
            case 0xE149: return 0x00CC;  //  capital I with grave accent
            case 0xE14F: return 0x00D2;  //  capital O with grave accent
            case 0xE155: return 0x00D9;  //  capital U with grave accent
            case 0xE157: return 0x1E80;  //  capital w with grave
            case 0xE159: return 0x1EF2;  //  capital y with grave
            case 0xE161: return 0x00E0;  //  small a with grave accent
            case 0xE165: return 0x00E8;  //  small e with grave accent
            case 0xE169: return 0x00EC;  //  small i with grave accent
            case 0xE16F: return 0x00F2;  //  small o with grave accent
            case 0xE175: return 0x00F9;  //  small u with grave accent
            case 0xE177: return 0x1E81;  //  small w with grave
            case 0xE179: return 0x1EF3;  //  small y with grave
            case 0xE220: return 0x02CA;  //  modifier letter acute accent
            case 0xE241: return 0x00C1;  //  capital A with acute accent
            case 0xE243: return 0x0106;  //  capital C with acute accent
            case 0xE245: return 0x00C9;  //  capital E with acute accent
            case 0xE247: return 0x01F4;  //  capital g with acute
            case 0xE249: return 0x00CD;  //  capital I with acute accent
            case 0xE24B: return 0x1E30;  //  capital k with acute
            case 0xE24C: return 0x0139;  //  capital L with acute accent
            case 0xE24D: return 0x1E3E;  //  capital m with acute
            case 0xE24E: return 0x0143;  //  capital N with acute accent
            case 0xE24F: return 0x00D3;  //  capital O with acute accent
            case 0xE250: return 0x1E54;  //  capital p with acute
            case 0xE252: return 0x0154;  //  capital R with acute accent
            case 0xE253: return 0x015A;  //  capital S with acute accent
            case 0xE255: return 0x00DA;  //  capital U with acute accent
            case 0xE257: return 0x1E82;  //  capital w with acute
            case 0xE259: return 0x00DD;  //  capital Y with acute accent
            case 0xE25A: return 0x0179;  //  capital Z with acute accent
            case 0xE261: return 0x00E1;  //  small a with acute accent
            case 0xE263: return 0x0107;  //  small c with acute accent
            case 0xE265: return 0x00E9;  //  small e with acute accent
            case 0xE267: return 0x01F5;  //  small g with acute
            case 0xE269: return 0x00ED;  //  small i with acute accent
            case 0xE26B: return 0x1E31;  //  small k with acute
            case 0xE26C: return 0x013A;  //  small l with acute accent
            case 0xE26D: return 0x1E3F;  //  small m with acute
            case 0xE26E: return 0x0144;  //  small n with acute accent
            case 0xE26F: return 0x00F3;  //  small o with acute accent
            case 0xE270: return 0x1E55;  //  small p with acute
            case 0xE272: return 0x0155;  //  small r with acute accent
            case 0xE273: return 0x015B;  //  small s with acute accent
            case 0xE275: return 0x00FA;  //  small u with acute accent
            case 0xE277: return 0x1E83;  //  small w with acute
            case 0xE279: return 0x00FD;  //  small y with acute accent
            case 0xE27A: return 0x017A;  //  small z with acute accent
            case 0xE2A5: return 0x01FC;  //  capital ae with acute
            case 0xE2B5: return 0x01FD;  //  small ae with acute
            case 0xE320: return 0x02C6;  //  modifier letter circumflex accent
            case 0xE341: return 0x00C2;  //  capital A with circumflex accent
            case 0xE343: return 0x0108;  //  capital c with circumflex
            case 0xE345: return 0x00CA;  //  capital E with circumflex accent
            case 0xE347: return 0x011C;  //  capital g with circumflex
            case 0xE348: return 0x0124;  //  capital h with circumflex
            case 0xE349: return 0x00CE;  //  capital I with circumflex accent
            case 0xE34A: return 0x0134;  //  capital j with circumflex
            case 0xE34F: return 0x00D4;  //  capital O with circumflex accent
            case 0xE353: return 0x015C;  //  capital s with circumflex
            case 0xE355: return 0x00DB;  //  capital U with circumflex
            case 0xE357: return 0x0174;  //  capital w with circumflex
            case 0xE359: return 0x0176;  //  capital y with circumflex
            case 0xE35A: return 0x1E90;  //  capital z with circumflex
            case 0xE361: return 0x00E2;  //  small a with circumflex accent
            case 0xE363: return 0x0109;  //  small c with circumflex
            case 0xE365: return 0x00EA;  //  small e with circumflex accent
            case 0xE367: return 0x011D;  //  small g with circumflex
            case 0xE368: return 0x0125;  //  small h with circumflex
            case 0xE369: return 0x00EE;  //  small i with circumflex accent
            case 0xE36A: return 0x0135;  //  small j with circumflex
            case 0xE36F: return 0x00F4;  //  small o with circumflex accent
            case 0xE373: return 0x015D;  //  small s with circumflex
            case 0xE375: return 0x00FB;  //  small u with circumflex
            case 0xE377: return 0x0175;  //  small w with circumflex
            case 0xE379: return 0x0177;  //  small y with circumflex
            case 0xE37A: return 0x1E91;  //  small z with circumflex
            case 0xE420: return 0x02DC;  //  small tilde
            case 0xE441: return 0x00C3;  //  capital A with tilde
            case 0xE445: return 0x1EBC;  //  capital e with tilde
            case 0xE449: return 0x0128;  //  capital i with tilde
            case 0xE44E: return 0x00D1;  //  capital N with tilde
            case 0xE44F: return 0x00D5;  //  capital O with tilde
            case 0xE455: return 0x0168;  //  capital u with tilde
            case 0xE456: return 0x1E7C;  //  capital v with tilde
            case 0xE459: return 0x1EF8;  //  capital y with tilde
            case 0xE461: return 0x00E3;  //  small a with tilde
            case 0xE465: return 0x1EBD;  //  small e with tilde
            case 0xE469: return 0x0129;  //  small i with tilde
            case 0xE46E: return 0x00F1;  //  small n with tilde
            case 0xE46F: return 0x00F5;  //  small o with tilde
            case 0xE475: return 0x0169;  //  small u with tilde
            case 0xE476: return 0x1E7D;  //  small v with tilde
            case 0xE479: return 0x1EF9;  //  small y with tilde
            case 0xE520: return 0x02C9;  //  modifier letter macron
            case 0xE541: return 0x0100;  //  capital a with macron
            case 0xE545: return 0x0112;  //  capital e with macron
            case 0xE547: return 0x1E20;  //  capital g with macron
            case 0xE549: return 0x012A;  //  capital i with macron
            case 0xE54F: return 0x014C;  //  capital o with macron
            case 0xE555: return 0x016A;  //  capital u with macron
            case 0xE561: return 0x0101;  //  small a with macron
            case 0xE565: return 0x0113;  //  small e with macron
            case 0xE567: return 0x1E21;  //  small g with macron
            case 0xE569: return 0x012B;  //  small i with macron
            case 0xE56F: return 0x014D;  //  small o with macron
            case 0xE575: return 0x016B;  //  small u with macron
            case 0xE5A5: return 0x01E2;  //  capital ae with macron
            case 0xE5B5: return 0x01E3;  //  small ae with macron
            case 0xE620: return 0x02D8;  //  breve
            case 0xE641: return 0x0102;  //  capital A with breve
            case 0xE645: return 0x0114;  //  capital e with breve
            case 0xE647: return 0x011E;  //  capital g with breve
            case 0xE649: return 0x012C;  //  capital i with breve
            case 0xE64F: return 0x014E;  //  capital o with breve
            case 0xE655: return 0x016C;  //  capital u with breve
            case 0xE661: return 0x0103;  //  small a with breve
            case 0xE665: return 0x0115;  //  small e with breve
            case 0xE667: return 0x011F;  //  small g with breve
            case 0xE669: return 0x012D;  //  small i with breve
            case 0xE66F: return 0x014F;  //  small o with breve
            case 0xE675: return 0x016D;  //  small u with breve
            case 0xE720: return 0x02D9;  //  dot above
            case 0xE742: return 0x1E02;  //  capital b with dot above
            case 0xE743: return 0x010A;  //  capital c with dot above
            case 0xE744: return 0x1E0A;  //  capital d with dot above
            case 0xE745: return 0x0116;  //  capital e with dot above
            case 0xE746: return 0x1E1E;  //  capital f with dot above
            case 0xE747: return 0x0120;  //  capital g with dot above
            case 0xE748: return 0x1E22;  //  capital h with dot above
            case 0xE749: return 0x0130;  //  capital i with dot above
            case 0xE74D: return 0x1E40;  //  capital m with dot above
            case 0xE74E: return 0x1E44;  //  capital n with dot above
            case 0xE750: return 0x1E56;  //  capital p with dot above
            case 0xE752: return 0x1E58;  //  capital r with dot above
            case 0xE753: return 0x1E60;  //  capital s with dot above
            case 0xE754: return 0x1E6A;  //  capital t with dot above
            case 0xE757: return 0x1E86;  //  capital w with dot above
            case 0xE758: return 0x1E8A;  //  capital x with dot above
            case 0xE759: return 0x1E8E;  //  capital y with dot above
            case 0xE75A: return 0x017B;  //  capital Z with dot above
            case 0xE762: return 0x1E03;  //  small b with dot above
            case 0xE763: return 0x010B;  //  small c with dot above
            case 0xE764: return 0x1E0B;  //  small d with dot above
            case 0xE765: return 0x0117;  //  small e with dot above
            case 0xE766: return 0x1E1F;  //  small f with dot above
            case 0xE767: return 0x0121;  //  small g with dot above
            case 0xE768: return 0x1E23;  //  small h with dot above
            case 0xE76D: return 0x1E41;  //  small m with dot above
            case 0xE76E: return 0x1E45;  //  small n with dot above
            case 0xE770: return 0x1E57;  //  small p with dot above
            case 0xE772: return 0x1E59;  //  small r with dot above
            case 0xE773: return 0x1E61;  //  small s with dot above
            case 0xE774: return 0x1E6B;  //  small t with dot above
            case 0xE777: return 0x1E87;  //  small w with dot above
            case 0xE778: return 0x1E8B;  //  small x with dot above
            case 0xE779: return 0x1E8F;  //  small y with dot above
            case 0xE77A: return 0x017C;  //  small z with dot above
            case 0xE820: return 0x00A8;  //  diaeresis
            case 0xE841: return 0x00C4;  //  capital A with diaeresis
            case 0xE845: return 0x00CB;  //  capital E with diaeresis
            case 0xE848: return 0x1E26;  //  capital h with diaeresis
            case 0xE849: return 0x00CF;  //  capital I with diaeresis
            case 0xE84F: return 0x00D6;  //  capital O with diaeresis
            case 0xE855: return 0x00DC;  //  capital U with diaeresis
            case 0xE857: return 0x1E84;  //  capital w with diaeresis
            case 0xE858: return 0x1E8C;  //  capital x with diaeresis
            case 0xE859: return 0x0178;  //  capital y with diaeresis
            case 0xE861: return 0x00E4;  //  small a with diaeresis
            case 0xE865: return 0x00EB;  //  small e with diaeresis
            case 0xE868: return 0x1E27;  //  small h with diaeresis
            case 0xE869: return 0x00EF;  //  small i with diaeresis
            case 0xE86F: return 0x00F6;  //  small o with diaeresis
            case 0xE874: return 0x1E97;  //  small t with diaeresis
            case 0xE875: return 0x00FC;  //  small u with diaeresis
            case 0xE877: return 0x1E85;  //  small w with diaeresis
            case 0xE878: return 0x1E8D;  //  small x with diaeresis
            case 0xE879: return 0x00FF;  //  small y with diaeresis
            case 0xE920: return 0x02C7;  //  caron
            case 0xE941: return 0x01CD;  //  capital a with caron
            case 0xE943: return 0x010C;  //  capital C with caron
            case 0xE944: return 0x010E;  //  capital D with caron
            case 0xE945: return 0x011A;  //  capital E with caron
            case 0xE947: return 0x01E6;  //  capital g with caron
            case 0xE949: return 0x01CF;  //  capital i with caron
            case 0xE94B: return 0x01E8;  //  capital k with caron
            case 0xE94C: return 0x013D;  //  capital L with caron
            case 0xE94E: return 0x0147;  //  capital N with caron
            case 0xE94F: return 0x01D1;  //  capital o with caron
            case 0xE952: return 0x0158;  //  capital R with caron
            case 0xE953: return 0x0160;  //  capital S with caron
            case 0xE954: return 0x0164;  //  capital T with caron
            case 0xE955: return 0x01D3;  //  capital u with caron
            case 0xE95A: return 0x017D;  //  capital Z with caron
            case 0xE961: return 0x01CE;  //  small a with caron
            case 0xE963: return 0x010D;  //  small c with caron
            case 0xE964: return 0x010F;  //  small d with caron
            case 0xE965: return 0x011B;  //  small e with caron
            case 0xE967: return 0x01E7;  //  small g with caron
            case 0xE969: return 0x01D0;  //  small i with caron
            case 0xE96A: return 0x01F0;  //  small j with caron
            case 0xE96B: return 0x01E9;  //  small k with caron
            case 0xE96C: return 0x013E;  //  small l with caron
            case 0xE96E: return 0x0148;  //  small n with caron
            case 0xE96F: return 0x01D2;  //  small o with caron
            case 0xE972: return 0x0159;  //  small r with caron
            case 0xE973: return 0x0161;  //  small s with caron
            case 0xE974: return 0x0165;  //  small t with caron
            case 0xE975: return 0x01D4;  //  small u with caron
            case 0xE97A: return 0x017E;  //  small z with caron
            case 0xEA20: return 0x02DA;  //  ring above
            case 0xEA41: return 0x00C5;  //  capital A with ring above
            case 0xEA61: return 0x00E5;  //  small a with ring above
            case 0xEA75: return 0x016F;  //  small u with ring above
            case 0xEA77: return 0x1E98;  //  small w with ring above
            case 0xEA79: return 0x1E99;  //  small y with ring above
            case 0xEAAD: return 0x016E;  //  capital U with ring above
            case 0xED20: return 0x02BC;  //  modifier letter apostrophe
            case 0xEE20: return 0x02DD;  //  double acute accent
            case 0xEE4F: return 0x0150;  //  capital O with double acute
            case 0xEE55: return 0x0170;  //  capital U with double acute
            case 0xEE6F: return 0x0151;  //  small o with double acute
            case 0xEE75: return 0x0171;  //  small u with double acute
            case 0xF020: return 0x00B8;  //  cedilla
            case 0xF043: return 0x00C7;  //  capital C with cedilla
            case 0xF044: return 0x1E10;  //  capital d with cedilla
            case 0xF047: return 0x0122;  //  capital g with cedilla
            case 0xF048: return 0x1E28;  //  capital h with cedilla
            case 0xF04B: return 0x0136;  //  capital k with cedilla
            case 0xF04C: return 0x013B;  //  capital l with cedilla
            case 0xF04E: return 0x0145;  //  capital n with cedilla
            case 0xF052: return 0x0156;  //  capital r with cedilla
            case 0xF053: return 0x015E;  //  capital S with cedilla
            case 0xF054: return 0x0162;  //  capital T with cedilla
            case 0xF063: return 0x00E7;  //  small c with cedilla
            case 0xF064: return 0x1E11;  //  small d with cedilla
            case 0xF067: return 0x0123;  //  small g with cedilla
            case 0xF068: return 0x1E29;  //  small h with cedilla
            case 0xF06B: return 0x0137;  //  small k with cedilla
            case 0xF06C: return 0x013C;  //  small l with cedilla
            case 0xF06E: return 0x0146;  //  small n with cedilla
            case 0xF072: return 0x0157;  //  small r with cedilla
            case 0xF073: return 0x015F;  //  small s with cedilla
            case 0xF074: return 0x0163;  //  small t with cedilla
            case 0xF120: return 0x02DB;  //  ogonek
            case 0xF141: return 0x0104;  //  capital A with ogonek
            case 0xF145: return 0x0118;  //  capital E with ogonek
            case 0xF149: return 0x012E;  //  capital i with ogonek
            case 0xF14F: return 0x01EA;  //  capital o with ogonek
            case 0xF155: return 0x0172;  //  capital u with ogonek
            case 0xF161: return 0x0105;  //  small a with ogonek
            case 0xF165: return 0x0119;  //  small e with ogonek
            case 0xF169: return 0x012F;  //  small i with ogonek
            case 0xF16F: return 0x01EB;  //  small o with ogonek
            case 0xF175: return 0x0173;  //  small u with ogonek
            case 0xF241: return 0x1EA0;  //  capital a with dot below
            case 0xF242: return 0x1E04;  //  capital b with dot below
            case 0xF244: return 0x1E0C;  //  capital d with dot below
            case 0xF245: return 0x1EB8;  //  capital e with dot below
            case 0xF248: return 0x1E24;  //  capital h with dot below
            case 0xF249: return 0x1ECA;  //  capital i with dot below
            case 0xF24B: return 0x1E32;  //  capital k with dot below
            case 0xF24C: return 0x1E36;  //  capital l with dot below
            case 0xF24D: return 0x1E42;  //  capital m with dot below
            case 0xF24E: return 0x1E46;  //  capital n with dot below
            case 0xF24F: return 0x1ECC;  //  capital o with dot below
            case 0xF252: return 0x1E5A;  //  capital r with dot below
            case 0xF253: return 0x1E62;  //  capital s with dot below
            case 0xF254: return 0x1E6C;  //  capital t with dot below
            case 0xF255: return 0x1EE4;  //  capital u with dot below
            case 0xF256: return 0x1E7E;  //  capital v with dot below
            case 0xF257: return 0x1E88;  //  capital w with dot below
            case 0xF259: return 0x1EF4;  //  capital y with dot below
            case 0xF25A: return 0x1E92;  //  capital z with dot below
            case 0xF261: return 0x1EA1;  //  small a with dot below
            case 0xF262: return 0x1E05;  //  small b with dot below
            case 0xF264: return 0x1E0D;  //  small d with dot below
            case 0xF265: return 0x1EB9;  //  small e with dot below
            case 0xF268: return 0x1E25;  //  small h with dot below
            case 0xF269: return 0x1ECB;  //  small i with dot below
            case 0xF26B: return 0x1E33;  //  small k with dot below
            case 0xF26C: return 0x1E37;  //  small l with dot below
            case 0xF26D: return 0x1E43;  //  small m with dot below
            case 0xF26E: return 0x1E47;  //  small n with dot below
            case 0xF26F: return 0x1ECD;  //  small o with dot below
            case 0xF272: return 0x1E5B;  //  small r with dot below
            case 0xF273: return 0x1E63;  //  small s with dot below
            case 0xF274: return 0x1E6D;  //  small t with dot below
            case 0xF275: return 0x1EE5;  //  small u with dot below
            case 0xF276: return 0x1E7F;  //  small v with dot below
            case 0xF277: return 0x1E89;  //  small w with dot below
            case 0xF279: return 0x1EF5;  //  small y with dot below
            case 0xF27A: return 0x1E93;  //  small z with dot below
            case 0xF355: return 0x1E72;  //  capital u with diaeresis below
            case 0xF375: return 0x1E73;  //  small u with diaeresis below
            case 0xF441: return 0x1E00;  //  capital a with ring below
            case 0xF461: return 0x1E01;  //  small a with ring below
            case 0xF520: return 0x2017;  //  double line below
            case 0xF948: return 0x1E2A;  //  capital h with breve below
            case 0xF968: return 0x1E2B;  //  small h with breve below
            // FamilySearch Extensions
            case 0xFC20: return 0x002F;  //  space with diagonal stroke
            case 0xFC41: return 0x023A;  //  capital A with diagonal stroke
            case 0xFC43: return 0x023B;  //  capital C with diagonal stroke
            case 0xFC45: return 0x0246;  //  capital E with diagonal stroke
            case 0xFC4B: return 0xA742;  //  capital K with diagonal stroke
            case 0xFC4C: return 0x0141;  //  capital L with diagonal stroke
            case 0xFC4F: return 0x00D8;  //  capital O with diagonal stroke
            case 0xFC51: return 0xA758;  //  capital Q with diagonal stroke
            case 0xFC54: return 0x023E;  //  capital T with diagonal stroke
            case 0xFC56: return 0xA75E;  //  capital V with diagonal stroke
            case 0xFC61: return 0x2C65;  //  small a with diagonal stroke
            case 0xFC63: return 0x023C;  //  small c with diagonal stroke
            case 0xFC65: return 0x0247;  //  small e with diagonal stroke
            case 0xFC6B: return 0xA743;  //  small k with diagonal stroke
            case 0xFC6C: return 0x0142;  //  small l with diagonal stroke
            case 0xFC6F: return 0x00F8;  //  small o with diagonal stroke
            case 0xFC71: return 0xA759;  //  small q with diagonal stroke
            case 0xFC74: return 0x2C66;  //  small t with diagonal stroke
            case 0xFC76: return 0xA75F;  //  small v with diagonal stroke
            case 0xD741: return 0x2550;	 // box drawings double horizontal
            case 0xD742: return 0x2551;	 // box drawings double vertical
            case 0xD743: return 0x2557;	 // box drawings double down and left
            case 0xD744: return 0x255D;	 // box drawings double up and left
            case 0xD745: return 0x255A;	 // box drawings double up and right
            case 0xD746: return 0x2554;	 // box drawings double down and right
            case 0xD747: return 0x2563;	 // box drawings double vertical and left
            case 0xD748: return 0x2569;	 // box drawings double up and horizontal
            case 0xD749: return 0x2560;	 // box drawings double vertical and right
            case 0xD74A: return 0x2566;	 // box drawings double down and horizontal
            case 0xD74B: return 0x256C;	 // box drawings double vertical and horizontal
            case 0xD74C: return 0x2562;	 // box drawings double vertical and single left
            case 0xD74D: return 0x2567;	 // box drawings single up and double horizontal
            case 0xD74E: return 0x255F;	 // box drawings double vertical and single right
            case 0xD74F: return 0x2564;	 // box drawings single down and double horizontal
            case 0xD750: return 0x2561;	 // box drawings single vertical and double left
            case 0xD751: return 0x2568;	 // box drawings double up and single horizontal
            case 0xD752: return 0x255E;	 // box drawings single vertical and double right
            case 0xD753: return 0x2565;	 // box drawings double down and single horizontal
            case 0xD754: return 0x256A;	 // box drawings single vertical and double horizontal
            case 0xD755: return 0x2591;	 // light shade - 25%
            case 0xD756: return 0x2592;	 // medium shade - 50%
            case 0xD757: return 0x2593;	 // dark shade - 75%
            case 0xD758: return 0x2588;	 // full block
            case 0xD761: return 0x2500;	 // box drawings light horizontal
            case 0xD762: return 0x2502;	 // box drawings light vertical
            case 0xD763: return 0x2510;	 // box drawings light down and left
            case 0xD764: return 0x2518;	 // box drawings light up and left
            case 0xD765: return 0x2514;	 // box drawings light up and right
            case 0xD766: return 0x250C;	 // box drawings light down and right
            case 0xD767: return 0x2524;	 // box drawings light vertical and left
            case 0xD768: return 0x2534;	 // box drawings light up and horizontal
            case 0xD769: return 0x251C;	 // box drawings light vertical and right
            case 0xD76A: return 0x252C;	 // box drawings light down and horizontal
            case 0xD76B: return 0x253C;	 // box drawings light vertical and horizontal
            case 0xD76C: return 0x2555;	 // box drawings light single down and double left
            case 0xD76D: return 0x255B;	 // box drawings light single up and double left
            case 0xD76E: return 0x2558;	 // box drawings light single up and double right
            case 0xD76F: return 0x2552;	 // box drawings light single down and double right
            case 0xD770: return 0x2556;	 // box drawings light double down and single left
            case 0xD771: return 0x255C;	 // box drawings light double up and single left
            case 0xD772: return 0x2559;	 // box drawings light double up and single right
            case 0xD773: return 0x2553;	 // box drawings light double down and single right
            case 0xD774: return 0x256B;	 // box drawings light double vertical and single horizontal
            case 0xD775: return 0x258C;	 // left half block
            case 0xD776: return 0x2580;	 // upper half block
            case 0xD777: return 0x2590;	 // right half block
            case 0xD778: return 0x2584;	 // lower half block
            case 0xD779: return 0x25AA;	 // black small square
            case 0xD824: return 0x03C2;	 // Greek small letter final sigma
            case 0xD841: return 0x0391;	 // Greek capital letter alpha
            case 0xD842: return 0x0392;	 // Greek capital letter beta
            case 0xD843: return 0x03A5;	 // Greek capital letter upsilon
            case 0xD844: return 0x0394;	 // Greek capital letter delta
            case 0xD845: return 0x0395;	 // Greek capital letter epsilon
            case 0xD846: return 0x03A6;	 // Greek capital letter phi
            case 0xD847: return 0x0393;	 // Greek capital letter gamma
            case 0xD848: return 0x03A8;	 // Greek capital letter psi
            case 0xD849: return 0x0399;	 // Greek capital letter iota
            case 0xD84B: return 0x039A;	 // Greek capital letter kappa
            case 0xD84C: return 0x039B;	 // Greek capital letter lamda
            case 0xD84D: return 0x039C;	 // Greek capital letter mu
            case 0xD84E: return 0x039D;	 // Greek capital letter nu
            case 0xD84F: return 0x039F;	 // Greek capital letter omicron
            case 0xD850: return 0x03A0;	 // Greek capital letter pi
            case 0xD851: return 0x03A7;	 // Greek capital letter chi
            case 0xD852: return 0x03A1;	 // Greek capital letter rho
            case 0xD853: return 0x03A3;	 // Greek capital letter sigma
            case 0xD854: return 0x03A4;	 // Greek capital letter tau
            case 0xD855: return 0x03A9;	 // Greek capital letter omega
            case 0xD856: return 0x0398;	 // Greek capital letter theta
            case 0xD857: return 0x0397;	 // Greek capital letter eta
            case 0xD858: return 0x039E;	 // Greek capital letter xi
            case 0xD85A: return 0x0396;	 // Greek capital letter zeta
            case 0xD861: return 0x03B1;	 // Greek small letter alpha
            case 0xD862: return 0x03B2;	 // Greek small letter beta
            case 0xD863: return 0x03C5;	 // Greek small letter upsilon
            case 0xD864: return 0x03B4;	 // Greek small letter delta
            case 0xD865: return 0x03B5;	 // Greek small letter epsilon
            case 0xD866: return 0x03C6;	 // Greek small letter phi
            case 0xD867: return 0x03B3;	 // Greek small letter gamma
            case 0xD868: return 0x03C8;	 // Greek small letter psi
            case 0xD869: return 0x03B9;	 // Greek small letter iota
            case 0xD86B: return 0x03BA;	 // Greek small letter kappa
            case 0xD86C: return 0x03BB;	 // Greek small letter lamda
            case 0xD86D: return 0x03BC;	 // Greek small letter mu
            case 0xD86E: return 0x03BD;	 // Greek small letter nu
            case 0xD86F: return 0x03BF;	 // Greek small letter omicron
            case 0xD870: return 0x03C0;	 // Greek small letter pi
            case 0xD871: return 0x03C7;	 // Greek small letter chi
            case 0xD872: return 0x03C1;	 // Greek small letter rho
            case 0xD873: return 0x03C3;	 // Greek small letter sigma
            case 0xD874: return 0x03C4;	 // Greek small letter tau
            case 0xD875: return 0x03C9;	 // Greek small letter omega
            case 0xD876: return 0x03B8;	 // Greek small letter theta
            case 0xD877: return 0x03B7;	 // Greek small letter eta
            case 0xD878: return 0x03BE;	 // Greek small letter xi
            case 0xD87A: return 0x03B6;	 // Greek small letter zeta
            case 0xD920: return 0x0E3F;	 // Thai currency symbol baht
            case 0xD921: return 0x00A2;	 // cent sign
            case 0xD922: return 0x00A5;	 // yen sign
            case 0xD923: return 0x20A7;	 // peseta sign
            case 0xD924: return 0x0192;	 // florin currency sign
            case 0xD925: return 0x00A4;	 // currency sign
            case 0xD926: return 0x20A4;	 // lira sign
            case 0xD927: return 0x20A0;	 // Euro-currency sign
            case 0xD928: return 0x20A1;	 // colon sign
            case 0xD929: return 0x20A2;	 // cruzeiro sign
            case 0xD92A: return 0x20A3;	 // French franc sign
            case 0xD92B: return 0x20A5;	 // mill sign
            case 0xD92C: return 0x20A6;	 // naira sign
            case 0xD92D: return 0x20A8;	 // rupee sign
            case 0xD92E: return 0x20A9;	 // won sign
            case 0xD92F: return 0x20AA;	 // new sheqel sign
            case 0xD930: return 0x20AC;	 // Euro sign
            case 0xD931: return 0x00B9;	 // superscript one
            case 0xD932: return 0x00B2;	 // superscript two
            case 0xD933: return 0x00B3;	 // superscript three
            case 0xD941: return 0x00AA;	 // feminine ordinal indicator
            case 0xD942: return 0x00BA;	 // masculine ordinal indicator
            case 0xD943: return 0x00AB;	 // left-pointing double angle quotation mark
            case 0xD944: return 0x00BB;	 // right-pointing double angle quotation mark
            case 0xD945: return 0x00A6;	 // broken vertical bar
            case 0xD946: return 0x00B6;	 // pilcrow/paragraph sign
            case 0xD947: return 0x00A7;	 // section sign
            case 0xD948: return 0x2310;	 // reversed not sign
            case 0xD949: return 0x2020;	 // dagger
            case 0xD94A: return 0x2021;	 // double dagger
            case 0xD94C: return 0x2122;	 // trade mark sign
            case 0xD94D: return 0xFB01;	 // Latin small ligature fi
            case 0xD94E: return 0xFB02;	 // Latin small ligature fl
            case 0xD94F: return 0x2039;	 // left-pointing single angle quotation mark
            case 0xD950: return 0x203A;	 // right-pointing single angle quotation mark
            case 0xD951: return 0x2030;	 // per mille sign
            case 0xD952: return 0x2026;	 // horizontal ellipsis
            case 0xD953: return 0x201C;	 // left double quotation mark
            case 0xD954: return 0x201D;	 // right double quotation mark
            case 0xD955: return 0x201A;	 // single low-9 quotation mark
            case 0xD956: return 0x201E;	 // double low-9 quotation mark
            case 0xD957: return 0x2022;	 // bullet
            case 0xD958: return 0x2013;	 // en dash
            case 0xD959: return 0x00A0;	 // no-break space
            case 0xD95A: return 0x00B5;	 // micro sign
            case 0xD961: return 0x00BD;	 // vulgar fraction one half
            case 0xD962: return 0x00BC;	 // vulgar fraction one quarter
            case 0xD963: return 0x00BE;	 // vulgar fraction three quarters
            case 0xD964: return 0x221E;	 // infinity
            case 0xD965: return 0x2205;	 // empty set
            case 0xD966: return 0x2208;	 // element of
            case 0xD967: return 0x2229;	 // intersection
            case 0xD968: return 0x00AC;	 // not sign
            case 0xD969: return 0x2261;	 // identical to
            case 0xD96A: return 0x00D7;	 // multiplication sign
            case 0xD96B: return 0x2265;	 // greater-than or equal to
            case 0xD96C: return 0x2264;	 // less-than or equal to
            case 0xD96D: return 0x00F7;	 // division sign
            case 0xD96E: return 0x2248;	 // almost equal to
            case 0xD96F: return 0x22C5;	 // dot operator
            case 0xD970: return 0x2320;	 // top half integral
            case 0xD971: return 0x2321;	 // bottom half integral
            case 0xD972: return 0x221A;	 // square root
            case 0xD973: return 0x2044;	 // fraction/division slash
            case 0xD974: return 0x2018;	 // left single quotation mark
            case 0xD975: return 0x2019;	 // right single quotation mark
            case 0xD976: return 0x2014;	 // em dash
            case 0xD977: return 0x00AD;	 // soft hyphen
            case 0xD979: return 0x20AD;	 // kip sign
            case 0xD97A: return 0x20AE;	 // tugrik sign
            case 0xD97B: return 0x20AF;	 // drachma sign
            case 0xD97C: return 0x20AB;	 // dong sign

            default: return -1;
        } //end switch
    }

}
