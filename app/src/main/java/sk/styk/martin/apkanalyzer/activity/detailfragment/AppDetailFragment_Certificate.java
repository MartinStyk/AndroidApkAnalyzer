package sk.styk.martin.apkanalyzer.activity.detailfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sk.styk.martin.apkanalyzer.R;
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment;
import sk.styk.martin.apkanalyzer.model.detail.CertificateData;
import sk.styk.martin.apkanalyzer.view.DetailItemView;

/**
 * @author Martin Styk
 * @version 22.06.2017.
 */
public class AppDetailFragment_Certificate extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail_certificate, container, false);

        CertificateData data = getArguments().getParcelable(AppDetailFragment.ARG_CHILD);

        ((DetailItemView) rootView.findViewById(R.id.item_sign_algorithm)).setValueText(data.getSignAlgorithm());

        String startDate = DateUtils.formatDateTime(getActivity(), data.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_start_date)).setValueText(startDate);

        String endDate = DateUtils.formatDateTime(getActivity(), data.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME);
        ((DetailItemView) rootView.findViewById(R.id.item_end_date)).setValueText(endDate);

        ((DetailItemView) rootView.findViewById(R.id.item_public_key_md5)).setValueText(data.getPublicKeyMd5());
        ((DetailItemView) rootView.findViewById(R.id.item_cert_md5)).setValueText(data.getCertificateHash());
        ((DetailItemView) rootView.findViewById(R.id.item_serial_number)).setValueText(String.valueOf(data.getSerialNumber()));
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_name)).setValueText(data.getIssuerName());
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_organization)).setValueText(String.valueOf(data.getIssuerOrganization()));
        ((DetailItemView) rootView.findViewById(R.id.item_issuer_country)).setValueText(data.getIssuerCountry());
        ((DetailItemView) rootView.findViewById(R.id.item_subject_name)).setValueText(String.valueOf(data.getSubjectName()));
        ((DetailItemView) rootView.findViewById(R.id.item_subject_organization)).setValueText(data.getSubjectOrganization());
        ((DetailItemView) rootView.findViewById(R.id.item_subject_country)).setValueText(data.getSubjectCountry());

        return rootView;
    }
}
