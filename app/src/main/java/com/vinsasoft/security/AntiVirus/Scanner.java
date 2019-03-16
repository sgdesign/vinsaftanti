package com.vinsasoft.security.AntiVirus;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Scanner
{
    public  static Set<GoodPackageResultData> scanForWhiteListedApps(List<PackageInfo> packagesToSearch, Set<PackageData> whiteListPackages,
                                                                 Set<GoodPackageResultData> result)
    {
        Set<GoodPackageResultData> subResult=new HashSet<GoodPackageResultData>();

        //Check against whitelist
        for(PackageData pd : whiteListPackages)
        {
            getPackagesByNameFilter(packagesToSearch, pd.getPackageName(), subResult);

            result.addAll(subResult);
        }

        return result;
    }

    public static boolean isAppWhiteListed(String packageName, Set<PackageData> whiteListPackages)
    {
        boolean matches=false;

        //Check against whitelist
        for(PackageData pd : whiteListPackages)
        {
            if(StaticTools.stringMatchesMask(packageName,pd.getPackageName()))
                return true;
        }

        return matches;
    }

     public static Set<IProblem> scanForBlackListedActivityApps(List<PackageInfo> packagesToSearch, Set<PackageData> blackListedActivityPackages,
                                                                        Set<IProblem> setToUpdate)
    {
        List<ActivityInfo> subResult=new ArrayList<ActivityInfo>();



        for(PackageInfo pi : packagesToSearch)
        {
            for(PackageData pd: blackListedActivityPackages)
            {
                getActivitiesByNameFilter(pi, pd.getPackageName(), subResult);

                if(subResult.size()>0)
                {
                    AppProblem bprd=getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                    if(bprd==null)
                    {
                        bprd = new AppProblem(pi.packageName);
                        setToUpdate.add(bprd);
                    }

                    for(ActivityInfo ai: subResult)
                    {
                        bprd.addActivityData(new ActivityData(ai.name));
                    }
                }
            }
        }
        return setToUpdate;
    }

    public static AppProblem scanForBlackListedActivityApp(PackageInfo pi,AppProblem bprdToFill,
                                                                     Set<PackageData> blackListedActivityPackages, List<ActivityInfo> arrayToRecycle)

    {
        for(PackageData pd: blackListedActivityPackages)
        {
            getActivitiesByNameFilter(pi, pd.getPackageName(), arrayToRecycle);

            if(arrayToRecycle.size()>0)
            {
                for(ActivityInfo ai: arrayToRecycle)
                {
                    bprdToFill.addActivityData(new ActivityData(ai.packageName));
                }
            }
        }

        return bprdToFill;
    }


     public static Set<IProblem> scanForSuspiciousPermissionsApps(List<PackageInfo> packagesToSearch, Set<PermissionData> suspiciousPermissions,
                                                                          Set<IProblem> setToUpdate)
    {
        AppProblem bprd=null;

        for(PackageInfo pi : packagesToSearch)
        {bprd=getBadPackageResultByPackageName(setToUpdate,pi.packageName);

            if(bprd==null)
                bprd=new AppProblem(pi.packageName);

            scanForSuspiciousPermissionsApp(pi, bprd, suspiciousPermissions);

            if(bprd.getPermissionData().size()>0)
                setToUpdate.add(bprd);
        }

        return setToUpdate;
    }

    public static AppProblem scanForSuspiciousPermissionsApp(PackageInfo pi, AppProblem bprdToFill,Set<PermissionData> suspiciousPermissions)
    {
        for(PermissionData permData : suspiciousPermissions)
        {
            if(StaticTools.packageInfoHasPermission(pi, permData.getPermissionName()))
            {
                bprdToFill.addPermissionData(permData);
            }
        }

        return bprdToFill;
    }


    public static AppProblem getBadPackageResultByPackageName(Set<IProblem> prd, String packageName)
    {
        AppProblem result=null;

        AppProblem temp=null;
        for (IProblem p : prd)
        {
            if(p.getType()== IProblem.ProblemType.AppProblem)
            {
                temp=(AppProblem)p;
                if(temp.getPackageName().equals(packageName))
                {
                    result = temp;
                    break;
                }
            }
        }

        return result;
    }



    public static Set<GoodPackageResultData> getPackagesByNameFilter(List<PackageInfo> packages, String filter, Set<GoodPackageResultData> result)
    {
        boolean wildcard=false;

        result.clear();

        if(filter.charAt(filter.length()-1)=='*')
            wildcard=true;

        PackageInfo packInfo =null;

        for (int i=0; i < packages.size(); i++)
        {
            packInfo=packages.get(i);

            if(StaticTools.stringMatchesMask(packInfo.packageName, filter))
            {
                result.add(new GoodPackageResultData(packInfo));

                if (!wildcard)
                    break;
            }
        }

        return result;
    }

    public static  List<ActivityInfo> getActivitiesByNameFilter(PackageInfo pi, String filter, List<ActivityInfo> result)
    {
        result.clear();

        if(pi.activities==null)
            return result;

        boolean wildcard=false;

        if(filter.charAt(filter.length()-1)=='*')
        {
            wildcard=true;
            filter=filter.substring(0,filter.length()-2);
        }
        else
            wildcard=false;

        ActivityInfo activityInfo =null;

        for (int i=0; i < pi.activities.length; i++)
        {
            activityInfo=pi.activities[i];

            if(activityInfo.name.startsWith(filter))
            {
                result.add(activityInfo);
            }
        }

        return result;
    }

    public static Set<IProblem> scanInstalledAppsFromGooglePlay(Context context,List<PackageInfo> packagesToSearch, Set<IProblem> setToUpdate)
    {
        for(PackageInfo pi : packagesToSearch)
        {
            if(!StaticTools.checkIfAppWasInstalledThroughGooglePlay(context,pi.packageName))
            {
                AppProblem bprd=Scanner.getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                if(bprd==null)
                {
                    bprd = new AppProblem(pi.packageName);
                    setToUpdate.add(bprd);
                }

                bprd.setInstalledThroughGooglePlay(false);
            }
            else
            {
                AppProblem bprd=Scanner.getBadPackageResultByPackageName(setToUpdate, pi.packageName);
                if(bprd!=null)
                    bprd.setInstalledThroughGooglePlay(true);
            }
        }

        return setToUpdate;
    }


    protected static AppProblem scanInstalledAppFromGooglePlay(Context context,AppProblem bprd)
    {
        if(!StaticTools.checkIfAppWasInstalledThroughGooglePlay(context,bprd.getPackageName()))
        {
            bprd.setInstalledThroughGooglePlay(false);
        }
        else
        {
            bprd.setInstalledThroughGooglePlay(true);
        }

        return bprd;
    }

    public static Set<IProblem> scanSystemProblems(Context context,UserWhiteList whiteList, Set<IProblem> setToUpdate)
    {
        if(StaticTools.checkIfUSBDebugIsEnabled(context) && !whiteList.checkIfSystemPackageInList(DebugUSBEnabledProblem.class))
        {
            DebugUSBEnabledProblem problem=new DebugUSBEnabledProblem();
            setToUpdate.add(problem);
        }

        if(StaticTools.checkIfUnknownAppIsEnabled(context) && !whiteList.checkIfSystemPackageInList(UnknownAppEnabledProblem.class))
        {
            UnknownAppEnabledProblem problem=new UnknownAppEnabledProblem();
            setToUpdate.add(problem);
        }

        return setToUpdate;
    }

}
