package dalcoms.pub.vibrationtuner;

public class VibPattern {
	final static long STEP_MSEC = 200;
	final static long MINOFF_MSEC = 5;//max_on = step_msec - minOff_msec
	final static long MINON_MSEC = 5;
	
	final private int[] stepDivide = {22,22,15,10,6,10,15};
	
	public VibPattern(){
		
	}
	
	public long[] getPattern(int vibeLevel){
		int[] stepTimes = {0,0,0,0,0,0,0};
		long[] pattern_array = new long[stepDivide.length*2];
		
		for(int i=0;i<vibeLevel;i++){
			if(stepTimes[i%stepTimes.length]<stepDivide[i%stepTimes.length]){
				stepTimes[i%stepTimes.length]+=1;
			}else{
				for(int ii=0;ii<stepTimes.length;ii++){
					if(stepTimes[ii]<stepDivide[ii]){
						stepTimes[ii] += 1;
						break;
					}
				}
			}
		}
		
		for(int k=0;k<stepTimes.length;k++){
			pattern_array[k*2+1] = MINON_MSEC+((stepTimes[k]*(STEP_MSEC-MINOFF_MSEC-MINON_MSEC))/stepDivide[k]);//#on
			pattern_array[k*2] = STEP_MSEC -pattern_array[k*2+1];//off
		}
		
		return pattern_array;
	}
}
